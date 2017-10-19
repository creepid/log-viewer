package com.github.logviewer.util.value;

/**
 * Created by rusakovich on 16.10.2017.
 */
public interface ConfigValueSource {
    /**
     * Returns the string value for given key or null if not available.
     *
     * @param key the key for config value
     * @return the string value for given key or null if not available
     */
    String getValue(String key);
}
