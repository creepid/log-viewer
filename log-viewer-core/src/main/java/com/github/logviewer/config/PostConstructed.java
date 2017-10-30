package com.github.logviewer.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicated the post constructor for a configured bean.
 * <p>
 * Created by rusakovich on 23.10.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PostConstructed {
    Class<? extends BeanPostConstructor<?>> constructor();
}
