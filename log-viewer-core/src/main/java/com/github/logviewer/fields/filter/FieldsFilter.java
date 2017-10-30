package com.github.logviewer.fields.filter;

import com.github.logviewer.config.ConfiguredBean;
import com.github.logviewer.fields.FieldBaseTypes;
import com.github.logviewer.fields.FieldsMap;
import com.github.logviewer.reader.FormatException;

import java.util.LinkedHashMap;

/**
 *
 * Filter for {@link FieldsMap}.
 *
 * Created by rusakovich on 30.10.2017.
 */
public interface FieldsFilter extends ConfiguredBean {
    /**
     * Filters passed fields.
     *
     * @param fields
     *            fields to filter
     */
    void filter(FieldsMap fields) throws FormatException;

    /**
     * Filters known fields.
     *
     * @param knownFields
     *            fields supported and known by a {@link LogEntryReader}
     */
    void filterKnownFields(LinkedHashMap<String, FieldBaseTypes> knownFields) throws FormatException;

}
