package com.github.logviewer.model;


import com.github.logviewer.model.support.LogInputStream;

import java.io.IOException;

/**
 * Created by rusakovich on 23.10.2017.
 */
public interface LogRawAccess<STREAMTYPE extends LogInputStream> extends LogPointerFactory {

    /**
     * Returns an input stream to read from the log beginning from the pointer.
     *
     * @param from
     *            the pointer to start the stream from; null indicates the log
     *            start.
     * @return log stream
     * @throws IOException
     *             in case of errors
     */
    STREAMTYPE getInputStream(LogPointer from) throws IOException;

    Navigation<?> getNavigation();

}
