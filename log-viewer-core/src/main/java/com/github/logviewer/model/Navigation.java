package com.github.logviewer.model;

import java.io.IOException;
import java.util.Date;

/**
 * Created by rusakovich on 23.10.2017.
 * <p>
 * Represents a strategy for navigating in a log.
 *
 * @param <M> metric type for absolute navigation
 */
public interface Navigation<M> {

    /**
     * Navigation types.
     */
    public static enum NavigationType {
        BYTE, DATE;
    }

    /**
     * Marker interface to navigate in byte offset oriented log.
     *
     */
    public static interface ByteOffsetNavigation extends Navigation<Long> {

    }

    /**
     * Marker interface to navigate in the log using timestamps.
     *
     */
    public static interface DateOffsetNavigation extends Navigation<Date> {

    }

    /**
     * Navigates absolutely to the desired position in the log.
     *
     * @param offset the offset to navigate to
     * @return the target pointer
     * @throws IOException
     */
    LogPointerFactory.NavigationFuture absolute(M offset) throws IOException;
}
