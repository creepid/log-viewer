package com.github.logviewer.model.support;

import com.github.logviewer.model.LogPointer;

import java.io.IOException;

/**
 * Created by rusakovich on 23.10.2017.
 */
public interface LogInputStream {

    /**
     * Returns the actual position in a stream after read some data.
     *
     * @return actual position in a stream after read some data.
     */
    public abstract LogPointer getPointer() throws IOException;

}
