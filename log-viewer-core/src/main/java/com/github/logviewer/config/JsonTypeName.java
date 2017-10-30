package com.github.logviewer.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the type name of the annotated type used for JSON serialization and
 * deserialization.
 * <p>
 * Created by rusakovich on 23.10.2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonTypeName {
    /**
     * @return the type name used for serialization and deserialiaztion.
     */
    String value();

    /**
     * @return deprecated names used to resolve a type by older deprecated
     * names.
     */
    String[] deprecated() default {};
}