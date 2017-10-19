package com.github.logviewer.fields;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Mapps fields Java type to a serializable / deserializable type.
 * <p>
 * Created by rusakovich on 19.10.2017.
 */
public class FieldJsonMapper {
    /**
     * Resolves the Java type from given serialized type.
     *
     * @param type the simple type
     * @return the Java type if deserialization is supported for this type
     * otherwise null
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> resolveDeserializationType(final String type) {
        try {
            if (type != null) {
                final FieldBaseTypes baseType = FieldBaseTypes.valueOf(type);
                return (Class) baseType.getDeserializationType();
            }
        } catch (final IllegalArgumentException e) {
            // Accepted
        }
        return null;
    }

    /**
     * Resolves the type name and serialize as class from current field value,
     * the first used later for correct deserialization. The serialize as class
     * can be null, which indicates that serialization should follow without
     * type indication.
     *
     * @param fieldValue field value
     * @return simple type, in worst case {@link FieldBaseTypes#OBJECT} is
     * always returned as fallback
     */
    public <T> Pair<String, Class<Object>> resolveSerializationType(final T fieldValue) {
        final FieldBaseTypes type = FieldBaseTypes.resolveType(fieldValue);
        return Pair.of(type.name(), type.getSerializationType());
    }

}
