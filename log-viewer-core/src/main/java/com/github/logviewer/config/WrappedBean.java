package com.github.logviewer.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * Created by rusakovich on 23.10.2017.
 * <p>
 * Wrapper bean for lazy unmarshalling of configured beans. Specially used to
 * delegate JSON serialization to the wrapped bean.
 */
@JsonSerialize(using = WrappedBean.WrapperSerializer.class)
public interface WrappedBean<BeanType extends ConfiguredBean> extends
        ConfiguredBean {
    public static class WrapperSerializer extends
            JsonSerializer<WrappedBean<ConfiguredBean>> {

        @Override
        public void serialize(WrappedBean<ConfiguredBean> value,
                              JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            provider.defaultSerializeValue(value.getWrapped(), jgen);
        }

        @Override
        public void serializeWithType(WrappedBean<ConfiguredBean> value,
                                      JsonGenerator jgen, SerializerProvider provider,
                                      TypeSerializer typeSer) throws IOException,
                JsonProcessingException {
            provider.defaultSerializeValue(value.getWrapped(), jgen);
        }

    }

    BeanType getWrapped() throws ConfigException;
}
