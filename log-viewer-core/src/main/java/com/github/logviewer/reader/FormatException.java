package com.github.logviewer.reader;

import java.io.IOException;

/**
 * Reflects an exception in case of format errors during reading log entries.
 * <p>
 * Created by rusakovich on 30.10.2017.
 */
public class FormatException extends IOException {
    private static final long serialVersionUID = -7865748029157326999L;

    public FormatException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public FormatException(final String arg0) {
        super(arg0);
    }

}
