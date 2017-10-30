package com.github.logviewer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.logviewer.app.CoreAppConfig;
import com.github.logviewer.model.LogSource;
import com.github.logviewer.model.file.WildcardLogsSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by rusakovich on 23.10.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreAppConfig.class})
@Configuration
public class ConfiguredBeanJsonTest {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguredBeanJsonTest.class);

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testSerializing() throws IOException {
        WildcardLogsSource source = new WildcardLogsSource();
        source.setName("Test");
        String json = mapper.writeValueAsString(source);
        logger.info("Serialized bean: {}", json);

        // Deserialize
        LogSource source2 = mapper.readValue(json, LogSource.class);
        Assert.assertEquals(WildcardLogsSource.class, source2.getClass());
    }


}