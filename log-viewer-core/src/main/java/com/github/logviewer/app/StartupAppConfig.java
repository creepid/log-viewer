package com.github.logviewer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * App config for startup routines.
 * <p>
 * Created by rusakovich on 19.10.2017.
 */
@Configuration
public class StartupAppConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${logviewer.home}")
    private String logViewerHomeDir;

    @Value("${logviewer.version}")
    private String version;

    /**
     * Checks the home dir under ${logviewerhome} for write access, creates it
     * if necessary and writes a template config.properties.
     *
     * @return home directory representation
     * @throws Exception
     */
    @Bean
    public LogViewerHome homeDir() throws Exception {
        File logViewerHomeDirFile = new File(logViewerHomeDir);
        logger.info("Starting LogViewer {} with home directory {}", version, logViewerHomeDirFile.getPath());

        if (!logViewerHomeDirFile.exists()) {
            logger.info("Home directory is't present, going to create it");

            try {
                logViewerHomeDirFile.mkdirs();
            } catch (Exception e) {
                logger.error("Failed to create home directory \""
                                + logViewerHomeDirFile.getPath()
                                + "\". LogViewer can't operate without a write enabled home directory. Please create the home directory manually and grant the user Logviewer is running as the write access.",
                        e);
                throw e;
            }

        } else if (!logViewerHomeDirFile.canWrite()) {
            logger.error("Configured home directory \"{}\" isn't write enabled. LogViewer can't operate without a write enabled home directory. Please grant the user Logsniffer is running as the write access.",
                    logViewerHomeDirFile.getPath());
            throw new SecurityException("Configured home directory \""
                    + logViewerHomeDirFile.getPath()
                    + "\" isn't write enabled.");
        }

        File homeConfigProps = new File(logViewerHomeDirFile, "config.properties");
        if (!homeConfigProps.exists()) {
            FileOutputStream fo = new FileOutputStream(homeConfigProps);
            try {
                new Properties().store(fo, "Place here LogViewer settings");
            } finally {
                fo.close();
            }
        }

        return new LogViewerHome() {
            @Override
            public File getHomeDir() {
                return new File(logViewerHomeDir);
            }
        };
    }

}
