package com.github.logviewer.app;

import com.github.logviewer.util.value.ConfigInjector;
import com.github.logviewer.util.value.support.PropertiesBasedSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * Created by rusakovich on 16.10.2017.
 */
@Configuration
public class ConfigValueAppConfig {

    public static final String LOGSNIFFER_BASE_URL = "logviewer.baseUrl";

    @Bean
    public ConfigInjector configInjector() {
        return new ConfigInjector();
    }

    @Bean
    public PropertiesBasedSource propertiesBasedSource() {
        return new PropertiesBasedSource();
    }

    @Bean
    public ConversionService conversionService() {
        return new DefaultFormattingConversionService();
    }
}
