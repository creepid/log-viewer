package com.github.logviewer.fields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.logviewer.app.CoreAppConfig;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.*;

/**
 * Created by rusakovich on 19.10.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CoreAppConfig.class })
@Configuration
public class FieldsMapJsonTest {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testSerialization() throws JsonProcessingException {
        final FieldsMap map = new FieldsMap();
        map.put("fa", new Date(0));
        final String jsonStr = mapper.writeValueAsString(map);
        LOGGER.info("Serialized {} to: {}", map, jsonStr);
        final JSONObject parsedJson = JSONObject.fromObject(jsonStr);
        Assert.assertNotNull(parsedJson.get("@types"));
        Assert.assertEquals(FieldBaseTypes.DATE.name(), parsedJson.getJSONObject("@types").getString("fa"));
        Assert.assertEquals(0, parsedJson.getInt("fa"));
    }
}