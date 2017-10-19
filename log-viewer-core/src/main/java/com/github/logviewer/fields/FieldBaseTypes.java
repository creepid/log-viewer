package com.github.logviewer.fields;

import com.github.logviewer.model.LogEntry;
import com.github.logviewer.model.LogPointer;
import com.github.logviewer.model.SeverityLevel;
import com.github.logviewer.model.support.JsonLogPointer;
import com.google.common.primitives.Primitives;

import java.awt.List;
import java.util.Collection;
import java.util.Date;



/**
 * Created by rusakovich on 16.10.2017.
 */
public enum FieldBaseTypes {
    BYTE(byte.class),
    BOOLEAN(boolean.class),
    STRING(String.class),
    DATE(Date.class),
    SEVERITY(SeverityLevel.class),
    INTEGER(int.class),
    LONG(long.class),
    FLOAT(float.class),
    DOUBLE(double.class),
    LPOINTER(LogPointer.class, JsonLogPointer.class),
    LENTRY(LogEntry.class),
    FIELDS_MAP(FieldsMap.class), LIST(Collection.class, List.class), OBJECT(null);

    private Class<?> deserializationType;
    private Class<?> serializationType;

    FieldBaseTypes(final Class<?> javaType) {
        this.deserializationType = javaType;
        this.serializationType = javaType;
    }

    FieldBaseTypes(final Class<?> serializationType, final Class<?> deserializationType) {
        this.serializationType = serializationType;
        this.deserializationType = deserializationType;
    }

    public static FieldBaseTypes resolveType(final Object v) {
        if (v != null) {
            for (final FieldBaseTypes t : values()) {
                if (t.serializationType == null) {
                    continue;
                }
                Class<?> vc = v.getClass();
                if (Primitives.isWrapperType(vc)) {
                    vc = Primitives.unwrap(vc);
                }
                if (t.serializationType.isAssignableFrom(vc)) {
                    return t;
                }
            }
        }
        return OBJECT;
    }

    /**
     * @return the deserializationType
     */
    @SuppressWarnings("unchecked")
    public <T> Class<? super T> getDeserializationType() {
        return (Class<? super T>) deserializationType;
    }

    /**
     * @return the deserializationType
     */
    @SuppressWarnings("unchecked")
    public Class<Object> getSerializationType() {
        return (Class<Object>) serializationType;
    }

}
