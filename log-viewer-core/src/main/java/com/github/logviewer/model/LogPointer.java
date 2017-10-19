package com.github.logviewer.model;

/**
 * Created by rusakovich on 19.10.2017.
 */

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Abstraction of pointing a byte position inside a log. The
 * {@link #equals(Object)} method has to be implemented properly to compare
 * pointers.
 */
@JsonSerialize(as = LogPointer.class)
public interface LogPointer {
    /**
     * @return true if this pointer represents the start of log
     */
    public boolean isSOF();

    /**
     * @return true if this pointer represents the end of log
     */
    public boolean isEOF();

    /**
     * Returns an JSON serialized representation of this pointer.
     *
     * @return an JSON serialized representation of this pointer
     */
    @JsonRawValue
    public String getJson();
}
