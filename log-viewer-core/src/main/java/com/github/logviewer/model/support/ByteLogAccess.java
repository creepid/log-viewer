package com.github.logviewer.model.support;

import com.github.logviewer.model.LogPointer;
import com.github.logviewer.model.LogRawAccess;
import com.github.logviewer.model.Navigation;

import java.io.IOException;

/**
 * Created by rusakovich on 23.10.2017.
 */
public interface ByteLogAccess extends LogRawAccess<ByteLogInputStream>, Navigation.ByteOffsetNavigation {
    /**
     * Creates a position pointer in the log relative to the source pointer. A
     * null source means the log start position. The calculated pointer will
     * never leave the start or end bound of the log.
     *
     * @param source
     *            source position or null for log start position
     * @param relativeBytePosition
     *            bytes to move the pointer relative to the source position. A
     *            negative number moves the pointer to the log start, a positive
     *            number to the end.
     * @return new pointer
     * @throws IOException
     *             in case of errors
     */
    public abstract LogPointer createRelative(LogPointer source, long relativeBytePosition) throws IOException;

}
