package com.github.logviewer.util.value.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.github.logviewer.app.CoreAppConfig;
import com.github.logviewer.app.LogViewerHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.logviewer.util.value.ConfigValueStore;



/**
 * Created by rusakovich on 16.10.2017.
 */
public class PropertiesBasedSource implements ConfigValueStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesBasedSource.class);

    @Autowired
    private LogViewerHome homeDir;

    @Autowired
    @Qualifier(CoreAppConfig.BEAN_LOGVIEWER_PROPS)
    private Properties logviewerProperties;

    @Override
    public String getValue(final String key) {
        return logviewerProperties.getProperty(key);
    }

    @Override
    public void store(final String key, final String value) throws IOException {
        if (value != null) {
            logviewerProperties.setProperty(key, value);
        } else {
            logviewerProperties.remove(key);
        }

        File file = new File(homeDir.getHomeDir(), CoreAppConfig.LOGVIEWER_PROPERTIES_FILE);
        LOGGER.info("Saving config value for key '{}' to file: {}", key, file.getAbsolutePath());

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            LOGGER.warn("Failed to load current properties from file, continue with empty properties: "
                            + file.getAbsolutePath(), e);
        }

        if (value != null) {
            properties.setProperty(key, value);
        } else {
            properties.remove(key);
        }

        properties.store(new FileOutputStream(file), null);
    }

}
