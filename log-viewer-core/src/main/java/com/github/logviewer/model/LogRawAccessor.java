package com.github.logviewer.model;


import com.github.logviewer.model.support.LogInputStream;

import java.io.IOException;

/**
 * Created by rusakovich on 23.10.2017.
 */
public interface LogRawAccessor<ACCESSTYPE extends LogRawAccess<? extends LogInputStream>, LOGTYPE extends Log> {
    /**
     * Returns raw read access to the log associated with given path or null if
     * log not found.
     *
     * @param log
     *            log path
     * @return read access to associated log or null if log not found
     * @throws IOException
     *             in case of errors
     */
    public ACCESSTYPE getLogAccess(LOGTYPE log) throws IOException;
}
