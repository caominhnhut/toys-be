package com.momo.toys.be.service;

import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Document;

public interface StoreDocumentService {

    void store(Document document) throws FileStorageException;

    byte[] load(String filename) throws FileStorageException;
}
