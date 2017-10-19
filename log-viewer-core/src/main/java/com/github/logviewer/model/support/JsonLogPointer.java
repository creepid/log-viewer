package com.github.logviewer.model.support;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.logviewer.model.LogPointer;

/**
 * Created by rusakovich on 19.10.2017.
 * <p>
 * JSON nested transferable log pointer impl.
 */
public class JsonLogPointer implements LogPointer {
    @JsonRawValue
    private String json;

    private boolean sof;
    private boolean eof;

    public JsonLogPointer(final String json) {
        super();
        this.json = json;
    }

    public JsonLogPointer() {
        super();
    }

    @Override
    public String getJson() {
        return json;
    }

    /**
     * @param json the json to set
     */
    public void setJson(final JsonNode node) {
        this.json = node.toString();
    }

    @Override
    public boolean isSOF() {
        return sof;
    }

    @Override
    public boolean isEOF() {
        return eof;
    }

    /**
     * @param sof the sof to set
     */
    public void setSOF(final boolean sof) {
        this.sof = sof;
    }

    /**
     * @param eof the eof to set
     */
    public void setEOF(final boolean eof) {
        this.eof = eof;
    }

}
