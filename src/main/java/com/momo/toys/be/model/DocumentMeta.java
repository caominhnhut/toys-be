package com.momo.toys.be.model;

import org.bson.types.ObjectId;

public class DocumentMeta {

    private ObjectId documentId;

    private boolean isRequired;

    private String url;

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ObjectId getDocumentId(){
        return documentId;
    }

    public void setDocumentId(ObjectId documentId){
        this.documentId = documentId;
    }
}
