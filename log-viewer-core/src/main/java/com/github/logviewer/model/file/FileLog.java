package com.github.logviewer.model.file;

import com.github.logviewer.model.Log;

import java.io.File;

/**
 * Implements a file related log.
 * <p>
 * Created by rusakovich on 30.10.2017.
 */
public class FileLog implements Log {
    private final long lastModified;
    private final long size;
    private final String path;
    private final String name;

    public FileLog(final File file) {
        super();
        this.lastModified = file.lastModified();
        this.path = file.getPath();
        this.size = file.length();
        this.name = file.getName();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public long getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return "FileLog [file=" + path + "]";
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileLog other = (FileLog) obj;
        return path.equals(other.path);
    }

    @Override
    public SizeMetric getSizeMetric() {
        return SizeMetric.BYTE;
    }

}
