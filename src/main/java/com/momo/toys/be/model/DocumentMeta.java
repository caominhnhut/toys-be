package com.momo.toys.be.model;

public class DocumentMeta {

    private boolean isRequired;

    private String uri;

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
