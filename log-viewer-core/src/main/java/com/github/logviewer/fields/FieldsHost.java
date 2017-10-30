package com.github.logviewer.fields;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.logviewer.fields.filter.FieldsFilter;
import com.github.logviewer.reader.FormatException;
import com.github.logviewer.util.json.Views;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Hosts fields.
 * <p>
 * Created by rusakovich on 30.10.2017.
 */
@JsonIgnoreProperties(value = {"fieldTypes"}, allowGetters = true)
public interface FieldsHost {
    public static final class FieldHostUtils {
        /**
         * Utility class to filter field types provided by a host.
         *
         * @param targetHost the fields target host
         * @param filters    optional filters, can be null
         * @return filtered field types of the target host
         * @throws FormatException
         */
        public static LinkedHashMap<String, FieldBaseTypes> getFilteredFieldTypes(final FieldsHost targetHost,
                                                                                  final List<FieldsFilter> filters) throws FormatException {
            LinkedHashMap<String, FieldBaseTypes> fieldTypes;
            if (targetHost != null) {
                fieldTypes = new LinkedHashMap<>(targetHost.getFieldTypes());
            } else {
                fieldTypes = new LinkedHashMap<>();
            }
            if (filters != null) {
                for (final FieldsFilter f : filters) {
                    f.filterKnownFields(fieldTypes);
                }
            }
            return fieldTypes;
        }
    }

    /**
     * Returns fields and types recognized and supported by this reader.
     *
     * @return fields and types recognized and supported by this reader.
     * @throws FormatException
     */
    @JsonView(Views.Info.class)
    public LinkedHashMap<String, FieldBaseTypes> getFieldTypes() throws FormatException;
}
