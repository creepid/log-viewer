package com.github.logviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by rusakovich on 23.10.2017.
 */
@JsonTypeInfo(property = "@type", include = JsonTypeInfo.As.PROPERTY, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("common")
public interface Log {
    public static class SizeMetric {
        public static SizeMetric BYTE = new SizeMetric();
    }

    /**
     * Public name of the log, could be the same as the path.
     *
     * @return public name of the log
     */
    public String getName();

    /**
     * Path to the log file used as identifier.
     *
     * @return path to the log file
     */
    public String getPath();

    /**
     * @return size of the log related to the underlying metric
     */
    public long getSize();

    /**
     * @return metric size of log is measured
     */
    @JsonIgnore
    public SizeMetric getSizeMetric();

    /**
     * @return last modification date in analogy to {@link File#lastModified()}.
     */
    public long getLastModified();

}
