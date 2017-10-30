package com.github.logviewer.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.github.logviewer.app.ContextProvider;

import java.io.IOException;

/**
 * Created by rusakovich on 23.10.2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonTypeIdResolver(ConfiguredBean.ConfiguredBeanTypeIdResolver.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public interface ConfiguredBean {

    public static class ConfiguredBeanTypeIdResolver implements TypeIdResolver {

        private JavaType baseType;
        private ConfigBeanTypeResolver beanTypeResolver;

        @Override
        public void init(final JavaType baseType) {
            this.baseType = baseType;
            this.beanTypeResolver = ContextProvider.getContext().getBean(
                    ConfigBeanTypeResolver.class);
        }

        @Override
        public String idFromValue(final Object value) {
            return this.beanTypeResolver
                    .resolveTypeName(((ConfiguredBean) value).getClass());
        }

        @Override
        public String idFromValueAndType(final Object value,
                                         final Class<?> suggestedType) {
            return idFromValue(value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public String idFromBaseType() {
            return beanTypeResolver
                    .resolveTypeName((Class<ConfiguredBean>) baseType
                            .getRawClass());
        }

        @Override
        public JavaType typeFromId(final String id) {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public JavaType typeFromId(final DatabindContext context,
                                   final String id) {
            return context.constructType(beanTypeResolver.resolveTypeClass(id,
                    (Class<ConfiguredBean>) baseType.getRawClass()));
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return JsonTypeInfo.Id.NAME;
        }

    }

    /**
     * Delegates post construction for deserialized beans to
     * {@link BeanConfigFactoryManager#postConstruct(ConfiguredBean)}.
     *
     * @author mbok
     */
    public static class ConfiguredBeanDeserializer extends
            StdDeserializer<ConfiguredBean> implements ResolvableDeserializer {

        private static final long serialVersionUID = 8978550911628758105L;
        private final JsonDeserializer<?> defaultDeserializer;

        protected ConfiguredBeanDeserializer(
                final JsonDeserializer<?> defaultDeserializer) {
            super(ConfiguredBean.class);
            this.defaultDeserializer = defaultDeserializer;
        }

        @Override
        public ConfiguredBean deserialize(final JsonParser jp,
                                          final DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ConfiguredBean bean = (ConfiguredBean) defaultDeserializer
                    .deserialize(jp, ctxt);
            ContextProvider.getContext()
                    .getBean(BeanConfigFactoryManager.class)
                    .postConstruct(bean);
            return bean;
        }

        // for some reason you have to implement ResolvableDeserializer when
        // modifying BeanDeserializer
        // otherwise deserializing throws JsonMappingException??
        @Override
        public void resolve(final DeserializationContext ctxt) throws JsonMappingException {
            if (defaultDeserializer instanceof ResolvableDeserializer) {
                ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
            }
        }
    }

}
