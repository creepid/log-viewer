package com.github.logviewer.app;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Import;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import javax.annotation.PreDestroy;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Elasticsearch app config.
 * <p>
 * Created by rusakovich on 10.11.2017.
 */
@Configuration
@Import(ConfigValueAppConfig.class)
public class ElasticSearchAppConfig {

    private final static Logger logger = LoggerFactory.getLogger(ElasticSearchAppConfig.class);

    @Autowired
    private LogViewerHome logViewerHome;

    @Value(value = "${logviewer.es.indexName}")
    private String indexName;

    private ClientConnection clientConnection;

    private static interface ClientConnection {

        public Client getClient();

        public void close();
    }


    /**
     * Indicates whether elasticsearch is operated locally as embedded instance
     * or by connecting to a remote cluster.
     */
    public enum EsOperatingType {
        EMBEDDED, REMOTE
    }

    /**
     * Remote address representation.
     */
    public static final class RemoteAddress {
        @NotEmpty
        private String host;
        @Min(1)
        private int port = 9300;

        /**
         * @return the host
         */
        public String getHost() {
            return host;
        }

        /**
         * @param host the host to set
         */
        public void setHost(final String host) {
            this.host = host;
        }

        /**
         * @return the port
         */
        public int getPort() {
            return port;
        }

        /**
         * @param port the port to set
         */
        public void setPort(final int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return host + ":" + port;
        }

    }

    /**
     * Bean for elasticsearch settings.
     */
    public static final class EsSettings {
        @NotNull
        private EsOperatingType operatingType = EsOperatingType.EMBEDDED;

        @Valid
        private List<RemoteAddress> remoteAddresses;

        /**
         * @return the operatingType
         */
        public EsOperatingType getOperatingType() {
            return operatingType;
        }

        /**
         * @param operatingType the operatingType to set
         */
        public void setOperatingType(final EsOperatingType operatingType) {
            this.operatingType = operatingType;
        }

        /**
         * @return the remoteAddresses
         */
        public List<RemoteAddress> getRemoteAddresses() {
            return remoteAddresses;
        }

        /**
         * @param remoteAddresses the remoteAddresses to set
         */
        public void setRemoteAddresses(final List<RemoteAddress> remoteAddresses) {
            this.remoteAddresses = remoteAddresses;
        }

    }

    /**
     * Holds settings for elasticsearch.
     */
    public static interface EsSettingsHolder {
        /**
         * Returns current es settings.
         *
         * @return current es settings
         */
        public EsSettings getSettings();

        /**
         * Stores new es settings and applies it to the elasticsearch
         * connection.
         *
         * @param settings
         * @throws IOException
         */
        public void storeSettings(final EsSettings settings) throws IOException;
    }

    public static interface EsClientBuilder {
        Client buildFromSettings(EsSettings settings);
    }


    /**
     * Client callback.
     *
     * @param <T> return type
     */
    public static interface ClientCallback<T> {
        /**
         * Executes callback code using an acquired client, which is closed
         * safely after the callback.
         *
         * @param client acquired client to use
         * @return return value
         */
        public T execute(Client client);
    }

    /**
     * Template helper class to centralize node/client requests.
     */
    public interface ElasticClientTemplate {
        public <T> T executeWithClient(final ClientCallback<T> callback);
    }


    private synchronized ClientConnection getClientConnection(final EsSettings settings) {
        if (clientConnection == null) {
            if (settings.getOperatingType() == EsOperatingType.EMBEDDED) {
                final Node localEmbeddedNode = buildLocalEmbeddedNode();
                final Client client = localEmbeddedNode.client();
                clientConnection = new ClientConnection() {
                    @Override
                    public Client getClient() {
                        return client;
                    }

                    @Override
                    public void close() {
                        logger.info("Closing local embedded elasticsearch node");
                        client.close();
                        localEmbeddedNode.close();
                    }
                };
            } else {
                logger.info("Establishing remote elasticsearch connection to: {}", settings.getRemoteAddresses());
                final TransportClient client = TransportClient.builder().build();
                for (final RemoteAddress a : settings.getRemoteAddresses()) {
                    try {
                        client.addTransportAddress(
                                new InetSocketTransportAddress(InetAddress.getByName(a.getHost()), a.getPort()));
                    } catch (final UnknownHostException e) {
                        logger.warn("Failed to resolve ES host, it'll be ignored: " + a.getHost(), e);
                    }
                }
                clientConnection = new ClientConnection() {

                    @Override
                    public Client getClient() {
                        return client;
                    }

                    @Override
                    public void close() {
                        client.close();
                        logger.info("Closing remote elasticsearch connection to: {}", settings.getRemoteAddresses());
                    }
                };
            }

            final Client client = clientConnection.getClient();
            client.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();
            // if (!client.admin().indices().exists(new
            // IndicesExistsRequest(indexName)).actionGet().isExists()) {
            // logger.info("Created elasticsearch index: {}", indexName);
            // client.admin().indices().create(new
            // CreateIndexRequest(indexName)).actionGet();
            // }

        }
        return clientConnection;
    }

    @PreDestroy
    public void closeCurrentClientConnection() {
        if (clientConnection != null) {
            clientConnection.close();
            clientConnection = null;
        }
    }

    private Node buildLocalEmbeddedNode() {
        final File esHomeDir = new File(logViewerHome.getHomeDir(), "elasticsearch");
        final File esDataDir = new File(esHomeDir, "data");

        logger.info("Preparing local elasticsearch node on data path: {}", esDataDir.getPath());
        esDataDir.mkdirs();

        final Settings settings = Settings.settingsBuilder()
                .put("node.name", "embedded")
                .put("path.home", esHomeDir.getPath())
                .put("http.enabled", false).build();

        final Node node = NodeBuilder.nodeBuilder()
                .settings(settings)
                .clusterName("embedded")
                .data(true)
                .local(true)
                .node();

        return node;
    }


}
