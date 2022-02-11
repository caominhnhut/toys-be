package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.model.Document;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Function;

public class DocumentMapper {

    public static Function<MultipartFile, Document> mapToDocument = file -> {

        Document document = new Document();

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        document.setFilename(filename);

        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());

        try {
            document.setFileContent(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("File not found");
        }

        return document;
    };

    public static Function<Document, DocumentEntity> mapToDocumentEntity = document -> {

        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setFilename(document.getFilename());
        documentEntity.setFileType(document.getFileType());
        documentEntity.setFileSize(document.getFileSize());
        documentEntity.setFileUri(document.getDocumentUrl());

        return documentEntity;
    };
}
