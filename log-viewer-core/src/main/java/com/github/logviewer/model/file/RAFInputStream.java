package com.github.logviewer.model.file;

import com.github.logviewer.model.LogPointer;
import com.github.logviewer.model.support.ByteLogInputStream;
import com.github.logviewer.model.support.DefaultPointer;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * Log input stream based on {@link RandomAccessFile}.
 *
 * Created by rusakovich on 30.10.2017.
 */
public class RAFInputStream extends ByteLogInputStream {

    private final RandomAccessFile file;
    private final long size;

    public RAFInputStream(final RandomAccessFile file, final long size) {
        this.file = file;
        this.size = size;
    }

    @Override
    public LogPointer getPointer() throws IOException {
        return new DefaultPointer(file.getFilePointer(), size);
    }

    public void seek(final long pos) throws IOException {
        file.seek(pos);
    }

    @Override
    public int read() throws IOException {
        if (file.getFilePointer() < size) {
            return file.read();
        } else {
            return -1;
        }
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final long p = file.getFilePointer();
        if (p >= size) {
            return -1;
        } else if (p + len > size) {
            return file.read(b, off, (int) (size - p));
        } else {
            return file.read(b, off, len);
        }
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.file.close();
        }
    }

}
