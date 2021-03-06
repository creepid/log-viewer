package com.github.logviewer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.logviewer.fields.FieldsMap;

import java.util.Date;

/**
 * Created by rusakovich on 19.10.2017.
 * <p>
 * Represents an entry in a log with native pointers.
 */
@JsonDeserialize(using = LogEntry.LogEntryTypeSafeDeserializer.class)
public final class LogEntry extends FieldsMap {

    private static final long serialVersionUID = 6930083682998388113L;

    /**
     * Field key for convenient method {@link #getStartOffset()}.
     */
    public static final String FIELD_START_OFFSET = "lf_startOffset";

    /**
     * Field key for convenient method {@link #getEndOffset()}.
     */
    public static final String FIELD_END_OFFSET = "lf_endOffset";

    /**
     * Field key for convenient method {@link #getSeverity()}.
     */
    public static final String FIELD_SEVERITY_LEVEL = "lf_severity";

    /**
     * Field key for convenient method {@link #getTimeStamp()}.
     */
    public static final String FIELD_TIMESTAMP = "lf_timestamp";

    /**
     * Field key for convenient method {@link #getRawContent()}.
     */
    public static final String FIELD_RAW_CONTENT = "lf_raw";

    /**
     * Field key for convenient method {@link #isUnformatted()}.
     */
    public static final String FIELD_UNFORMATTED = "lf_unformatted";

    // @JsonSerialize(typing = Typing.STATIC)
    // @JsonDeserialize(as = JsonLogPointer.class)
    // private LogPointer startOffset;
    // @JsonSerialize(typing = Typing.STATIC)
    // @JsonDeserialize(as = JsonLogPointer.class)
    // private LogPointer endOffset;

    /**
     * @return the startOffset
     */
    public LogPointer getStartOffset() {
        return (LogPointer) super.get(FIELD_START_OFFSET);
    }

    /**
     * @param startOffset the startOffset to set
     */
    public void setStartOffset(final LogPointer startOffset) {
        super.put(FIELD_START_OFFSET, startOffset);
    }

    /**
     * @return the endOffset
     */
    public LogPointer getEndOffset() {
        return (LogPointer) super.get(FIELD_END_OFFSET);
    }

    /**
     * @param endOffset the endOffset to set
     */
    public void setEndOffset(final LogPointer endOffset) {
        super.put(FIELD_END_OFFSET, endOffset);
    }

    /**
     * @return the fields extracted from {@link #getRawContent()}. Values
     * {@link Object#toString()} method reflects the unchanged text part
     * as extracted from {@link #getRawContent()}.
     */
    @Deprecated
    public final FieldsMap getFields() {
        return this;
    }

    /**
     * @return the content
     */
    public String getRawContent() {
        return (String) super.get(FIELD_RAW_CONTENT);
    }

    /**
     * @param content the content to set
     */
    public void setRawContent(final String content) {
        super.put(FIELD_RAW_CONTENT, content);
    }

    /**
     * @return the level
     */
    public final SeverityLevel getSeverity() {
        return (SeverityLevel) super.get(FIELD_SEVERITY_LEVEL);
    }

    /**
     * @param level the severity level to set
     */
    public void setSeverity(final SeverityLevel level) {
        super.put(FIELD_SEVERITY_LEVEL, level);
    }

    /**
     * @return the timeStamp
     */
    public final Date getTimeStamp() {
        return (Date) super.get(FIELD_TIMESTAMP);
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(final Date timeStamp) {
        super.put(FIELD_TIMESTAMP, timeStamp);
    }

    /**
     * Marks the log entry as unformatted.
     *
     * @param unformatted to set
     */
    public void setUnformatted(final boolean unformatted) {
        super.put(FIELD_UNFORMATTED, unformatted);
    }

    /**
     * Indicates if the log entry is unformatted.
     *
     * @return true and only if this log entry was explictly marked as
     * unformatted
     */
    public boolean isUnformatted() {
        final Object u = super.get(FIELD_UNFORMATTED);
        if (u instanceof Boolean) {
            return (Boolean) u;
        }
        return false;
    }

    /**
     * Type safe deserializer for {@link LogEntry}s.
     *
     * @author mbok
     */
    public static class LogEntryTypeSafeDeserializer extends FieldsMapTypeSafeDeserializer {

        @Override
        protected FieldsMap create() {
            return new LogEntry();
        }

    }
}
