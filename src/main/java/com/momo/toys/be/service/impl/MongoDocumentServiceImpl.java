package com.momo.toys.be.service.impl;

import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.service.DocumentService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.UnaryOperator;

@Service("mongoDocumentService")
public class MongoDocumentServiceImpl implements DocumentService {

    @Value("${document.download.uri}")
    private String downloadUri;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Override
    public Document upload(Document document) {

        String fileUri = buildDownloadUri.apply(document.getFilename());
        DBObject metaData = new BasicDBObject();
        metaData.put("fileUri", fileUri);

        ObjectId objectId = gridFsOperations.store(new ByteArrayInputStream(document.getFileContent()), document.getFilename(), metaData);
        document.setObjectId(objectId);
        document.setFileUri(fileUri);

        return document;
    }

    @Override
    public Document readFile(String filename) throws FileStorageException {

        GridFsResource fsResource = gridFsOperations.getResource(filename);

        byte[] content;
        try (InputStream inputStream = fsResource.getInputStream()) {
            content = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }

        Document document = new Document();
        document.setFileContent(content);

        return document;
    }

    private UnaryOperator<String> buildDownloadUri = filename -> downloadUri.concat(filename);
}
