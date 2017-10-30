package com.github.logviewer.reader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.logviewer.fields.FieldBaseTypes;
import com.github.logviewer.fields.FieldsHost;
import com.github.logviewer.fields.filter.FieldsFilter;
import com.github.logviewer.fields.filter.LogEntryFilter;
import com.github.logviewer.model.*;
import com.github.logviewer.model.support.LogInputStream;
import com.github.logviewer.util.json.Views;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Proxy {@link LogEntryReader} with filtering support.
 *
 * Created by rusakovich on 23.10.2017.
 */
public class FilteredLogEntryReader<ACCESSTYPE extends LogRawAccess<? extends LogInputStream>> implements LogEntryReader<ACCESSTYPE> {

    @JsonProperty
    @Valid
    private List<FieldsFilter> filters = new ArrayList<>();

    @Valid
    private LogEntryReader<ACCESSTYPE> targetReader;

    /**
     * Mixin to disables serialization for not configurable readers.
     *
     * @author mbok
     *
     * @param <ACCESSTYPE>
     */
    public static class FilteredLogEntryReaderWithNotConfigurableTarget<ACCESSTYPE extends LogRawAccess<? extends LogInputStream>>
            extends FilteredLogEntryReader<ACCESSTYPE> {

        @Override
        @JsonIgnore
        public LogEntryReader<ACCESSTYPE> getTargetReader() {
            return super.getTargetReader();
        }

    }

    public FilteredLogEntryReader() {
        super();
    }

    /**
     * Returns a {@link FilteredLogEntryReader} wrapping the given reader in
     * case filters are defined. In case filters are null or empty the current
     * reader is returned back without wrapping.
     *
     * @param targetReader
     *            the target reader to wrapp for filtering
     * @param filters
     *            filters maybe null or empty
     * @return a {@link FilteredLogEntryReader} wrapping the given reader in
     *         case filters are defined. In case filters are null or empty the
     *         current reader is returned back without wrapping.
     */
    public static <ACCESSTYPE extends LogRawAccess<? extends LogInputStream>> LogEntryReader<ACCESSTYPE> wrappIfNeeded(
            final LogEntryReader<ACCESSTYPE> targetReader, final List<FieldsFilter> filters) {
        if (filters != null && !filters.isEmpty()) {
            return new FilteredLogEntryReader<>(targetReader, filters);
        }
        return targetReader;
    }

    /**
     * @param targetReader
     * @param filters
     */
    @SuppressWarnings("unchecked")
    public FilteredLogEntryReader(final LogEntryReader<ACCESSTYPE> targetReader, final List<FieldsFilter> filters) {
        super();
        this.targetReader = targetReader;
        this.filters = (List<FieldsFilter>) (filters != null ? new ArrayList<>(filters) : new ArrayList<>());
    }

    /**
     * @return the targetReader
     */
    @JsonProperty
    public LogEntryReader<ACCESSTYPE> getTargetReader() {
        return targetReader;
    }

    /**
     * @return the filters
     */
    public List<FieldsFilter> getFilters() {
        return filters;
    }

    @Override
    public void readEntries(final Log log, final ACCESSTYPE logAccess, final LogPointer startOffset,
                            final LogEntryConsumer consumer) throws IOException, FormatException {
        targetReader.readEntries(log, logAccess, startOffset, new LogEntryConsumer() {
            @Override
            public boolean consume(final Log log, final LogPointerFactory pointerFactory, final LogEntry entry)
                    throws IOException {
                filterLogEntry(entry);
                return consumer.consume(log, pointerFactory, entry);
            }
        });

    }

    private void filterLogEntry(final LogEntry entry) throws FormatException {
        for (final FieldsFilter f : filters) {
            f.filter(entry);
        }
    }

    @Override
    @JsonView(Views.Info.class)
    public List<SeverityLevel> getSupportedSeverities() {
        List<SeverityLevel> severities;
        if (targetReader != null) {
            severities = new ArrayList<>(targetReader.getSupportedSeverities());
        } else {
            severities = new ArrayList<>();
        }
        if (filters != null) {
            for (final FieldsFilter f : filters) {
                if (f instanceof LogEntryFilter) {
                    ((LogEntryFilter) f).filterSupportedSeverities(severities);
                }
            }
            Collections.sort(severities);
        }
        return severities;
    }

    @Override
    @JsonView(Views.Info.class)
    public LinkedHashMap<String, FieldBaseTypes> getFieldTypes() throws FormatException {
        return FieldsHost.FieldHostUtils.getFilteredFieldTypes(targetReader, filters);
    }

    /**
     * @param filters
     *            the filters to set
     */
    public void setFilters(final List<FieldsFilter> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<FieldsFilter>();
    }

    /**
     * @param targetReader
     *            the targetReader to set
     */
    public void setTargetReader(final LogEntryReader<ACCESSTYPE> targetReader) {
        this.targetReader = targetReader;
    }

    @Override
    public void readEntriesReverse(final Log log, final ACCESSTYPE logAccess, final LogPointer startOffset,
                                   final com.github.logviewer.reader.LogEntryReader.LogEntryConsumer consumer) throws IOException {
        targetReader.readEntriesReverse(log, logAccess, startOffset, new LogEntryConsumer() {
            @Override
            public boolean consume(final Log log, final LogPointerFactory pointerFactory, final LogEntry entry)
                    throws IOException {
                filterLogEntry(entry);
                return consumer.consume(log, pointerFactory, entry);
            }
        });
    }

}
