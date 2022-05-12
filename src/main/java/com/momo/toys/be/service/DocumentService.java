package com.momo.toys.be.service;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.entity.ReviewEntity;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Document;

public interface DocumentService {

    Document upload(Document document);

    Document upload(Document document, ProductEntity productId);

    Document uploadComment(Document document, ReviewEntity review);

    Document readFile(String filename) throws FileStorageException;

    void delete(Document document) throws FileStorageException;
}
