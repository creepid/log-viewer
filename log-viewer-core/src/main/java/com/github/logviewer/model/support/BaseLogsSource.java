package com.github.logviewer.model.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.logviewer.fields.FieldsMap;
import com.github.logviewer.model.LogRawAccess;
import com.github.logviewer.model.LogSource;
import com.github.logviewer.model.Navigation;
import com.github.logviewer.reader.FilteredLogEntryReader;
import com.github.logviewer.util.json.Views;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * Created by rusakovich on 23.10.2017.
 */
public abstract class BaseLogsSource<ACCESSTYPE extends LogRawAccess<? extends LogInputStream>>
        implements LogSource<ACCESSTYPE> {

    @JsonProperty
    @JsonView(Views.Info.class)
    private long id;

    @JsonProperty
    @JsonView(Views.Info.class)
    @NotEmpty
    private String name;

    @JsonProperty
    @JsonView(Views.Info.class)
    @Valid
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private FilteredLogEntryReader<ACCESSTYPE> reader = new FilteredLogEntryReader<>();

    @JsonProperty
    @JsonView(Views.Info.class)
    private FieldsMap uiSettings = new FieldsMap();

    @JsonProperty
    @JsonView(Views.Info.class)
    protected boolean readerConfigurable = true;

    /**
     * @return the id
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the reader
     */
    @Override
    public FilteredLogEntryReader<ACCESSTYPE> getReader() {
        return reader;
    }

    /**
     * @param reader
     *            the reader to set
     */
    public void setReader(final FilteredLogEntryReader<ACCESSTYPE> reader) {
        this.reader = reader;
    }

    /**
     * @return the uiSettings
     */
    @Override
    public FieldsMap getUiSettings() {
        return uiSettings;
    }

    /**
     * @param uiSettings
     *            the uiSettings to set
     */
    public void setUiSettings(final FieldsMap uiSettings) {
        this.uiSettings = uiSettings;
    }

    /**
     * Indicates if the reader is configurable for this source. Sometimes a log
     * source brings its own reader.
     *
     * @return the readerConfigurable
     */
    public boolean isReaderConfigurable() {
        return readerConfigurable;
    }

    /**
     * Returns the natively supported navigation type by this source.
     *
     * @return the natively supported navigation type by this source.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Views.Info.class)
    public abstract Navigation.NavigationType getNavigationType();

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name;
    }
}
