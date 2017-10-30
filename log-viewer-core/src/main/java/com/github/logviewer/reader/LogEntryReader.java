package com.github.logviewer.reader;

import com.github.logviewer.config.ConfiguredBean;
import com.github.logviewer.fields.FieldsHost;
import com.github.logviewer.model.*;
import com.github.logviewer.model.support.LogInputStream;

import java.io.IOException;
import java.util.List;

/**
 * Created by rusakovich on 23.10.2017.
 * <p>
 * Format dependent log reader. Reading is performed pipeline like.
 */
public interface LogEntryReader<ACCESSORTYPE extends LogRawAccess<? extends LogInputStream>>
        extends ConfiguredBean, FieldsHost {

    /**
     * Consumer for log entries, called sequentially when a new entry was read.
     *
     * @author mbok
     */
    public static interface LogEntryConsumer {
        /**
         * Called to consume the new read log entry.
         *
         * @param log            the log
         * @param pointerFactory the pointer factory
         * @param entry          the read entry
         * @return return true to continue reading (if EOF isn't reached) or
         * false to interrupt further reading.
         * @throws IOException in case of any errors
         */
        boolean consume(Log log, LogPointerFactory pointerFactory, LogEntry entry) throws IOException;
    }

    /**
     * Reads non-blocking the log entries beginning with the byte offset in log.
     * The read entries will be propagated sequentially to the given consumer.
     * The method returns back when {@link LogEntryConsumer#consume(LogEntry)}
     * returns false or the boundary is reached.
     *
     * @param log         the log to read
     * @param logAccess   the access to the log to read from
     * @param startOffset the offset pointer in the log to start reading on. A null
     *                    value means start from beginning.
     * @param consumer    consumer to propagate read entries to
     */
    public void readEntries(Log log, ACCESSORTYPE logAccess, LogPointer startOffset, LogEntryConsumer consumer)
            throws IOException;

    /**
     * Reads log entries in a reverse order beginning from the given offset. The
     * read entries will be propagated sequentially to the given consumer. The
     * method returns back when {@link LogEntryConsumer#consume(LogEntry)}
     * returns false or the boundary is reached.
     *
     * @param log         the log to read
     * @param logAccess   the access to the log to read from
     * @param startOffset the offset pointer in the log to start reading on. A null
     *                    value means start from beginning.
     * @param consumer    consumer to propagate read entries to
     */
    public void readEntriesReverse(Log log, ACCESSORTYPE logAccess, LogPointer startOffset, LogEntryConsumer consumer)
            throws IOException;

    /**
     * @return list of supported and provided severity levels.
     */
    public List<SeverityLevel> getSupportedSeverities();

    /**
     * Wrapper for delegated log entry reader e.g. to allow lazy initiating of
     * readers.
     *
     * @param <ContentType> the entry type
     * @author mbok
     */
    public static abstract class LogEntryReaderWrapper
            implements LogEntryReader<LogRawAccess<? extends LogInputStream>> {
        private LogEntryReader<LogRawAccess<? extends LogInputStream>> wrapped;

        protected abstract LogEntryReader<LogRawAccess<? extends LogInputStream>> getWrapped()
                throws IOException, FormatException;

        private LogEntryReader<LogRawAccess<? extends LogInputStream>> getReader() throws IOException, FormatException {
            if (wrapped == null) {
                wrapped = getWrapped();
            }
            return wrapped;
        }

        @Override
        public void readEntries(final Log log, final LogRawAccess<? extends LogInputStream> logAccess,
                                final LogPointer startOffset, final LogEntryConsumer consumer) throws IOException, FormatException {
            getReader().readEntries(log, logAccess, startOffset, consumer);
        }

        @Override
        public List<SeverityLevel> getSupportedSeverities() {
            try {
                return getReader().getSupportedSeverities();
            } catch (final Exception e) {
                throw new RuntimeException("Unexpected", e);
            }
        }

    }
}
