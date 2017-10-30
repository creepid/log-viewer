package com.github.logviewer.config;

/**
 * Created by rusakovich on 23.10.2017.
 */

/**
 * Reflects a miss-configuration exception.
 *
 * @author rusakovich
 */
public class ConfigException extends RuntimeException {
    private static final long serialVersionUID = -6997110654319860550L;

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(String message) {
        super(message);
    }

}
