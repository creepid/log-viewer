package com.github.logviewer.fields.filter;

import com.github.logviewer.model.SeverityLevel;

import java.util.List;

/**
 * Additional filter capabilities for {@link LogEntry}s.
 * <p>
 * Created by rusakovich on 30.10.2017.
 */
public interface LogEntryFilter extends FieldsFilter {
    /**
     * Filters supported severities.
     *
     * @param severities fields supported by a {@link LogEntryReader}.
     */
    void filterSupportedSeverities(List<SeverityLevel> severities);
}
