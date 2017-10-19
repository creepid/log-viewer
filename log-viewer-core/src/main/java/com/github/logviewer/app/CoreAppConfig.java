package com.github.logviewer.app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.type.MapType;
import com.github.logviewer.fields.FieldsMap;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by rusakovich on 16.10.2017.
 */
@Configuration
@Import({ConfigValueAppConfig.class})
public class CoreAppConfig {

    public static final String BEAN_LOGVIEWER_PROPS = "logViewerProps";
    public static final String LOGVIEWER_PROPERTIES_FILE = "config.properties";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext context;

    /**
     * Registers the {@link ContextProvider}.
     *
     * @return the context provider.
     */
    @Bean
    public ContextProvider contextProvider() {
        ContextProvider.setContext(context);
        return new ContextProvider();
    }

    @Bean(name = {BEAN_LOGVIEWER_PROPS})
    @Autowired
    public PropertiesFactoryBean logViewerProperties(final ApplicationContext ctx) throws IOException {
        if (ctx.getEnvironment().acceptsProfiles("!" + ContextProvider.PROFILE_NONE_QA)) {
            final File qaFile = File.createTempFile("logviewer", "qa");
            qaFile.delete();
            final String qaHomeDir = qaFile.getPath();
            logger.info("QA mode active, setting random home directory: {}", qaHomeDir);
            System.setProperty("logviewer.home", qaHomeDir);
        }

        final PathMatchingResourcePatternResolver pathMatcher = new PathMatchingResourcePatternResolver();
        Resource[] classPathProperties = pathMatcher.getResources("classpath*:/config/**/logviewer-*.properties");
        final Resource[] metainfProperties = pathMatcher
                .getResources("classpath*:/META-INF/**/logviewer-*.properties");

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        for (final Resource resource : metainfProperties) {
            classPathProperties = (Resource[]) ArrayUtils.add(classPathProperties, resource);
        }

        classPathProperties = (Resource[]) ArrayUtils.add(classPathProperties,
                new FileSystemResource(System.getProperty("logviewer.home") + "/" + LOGVIEWER_PROPERTIES_FILE));

        propertiesFactoryBean.setLocations(classPathProperties);
        propertiesFactoryBean.setProperties(System.getProperties());
        propertiesFactoryBean.setLocalOverride(true);
        propertiesFactoryBean.setIgnoreResourceNotFound(true);

        return propertiesFactoryBean;
    }

    /**
     * Returns a general properties placeholder configurer
     *
     * @param props autowired logViewerProperties bean
     * @return A general properties placeholder configurer.
     * @throws IOException
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Autowired
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(
            @Qualifier(BEAN_LOGVIEWER_PROPS) final Properties props) throws IOException {
        final PropertyPlaceholderConfigurer propertiyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertiyPlaceholderConfigurer.setIgnoreResourceNotFound(true);
        propertiyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        propertiyPlaceholderConfigurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        propertiyPlaceholderConfigurer.setProperties(props);
        return propertiyPlaceholderConfigurer;
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        final ObjectMapper jsonMapper = new ObjectMapper();

        jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        jsonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        jsonMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

        final SimpleModule module = new SimpleModule("FieldsMapping", Version.unknownVersion());
        module.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifyMapSerializer(final SerializationConfig config, final MapType valueType,
                                                         final BeanDescription beanDesc, final JsonSerializer<?> serializer) {
                if (FieldsMap.class.isAssignableFrom(valueType.getRawClass())) {
                    return new FieldsMap.FieldsMapMixInLikeSerializer();
                } else {
                    return super.modifyMapSerializer(config, valueType, beanDesc, serializer);
                }
            }
        });

        jsonMapper.registerModule(module);
        return jsonMapper;
    }


}
