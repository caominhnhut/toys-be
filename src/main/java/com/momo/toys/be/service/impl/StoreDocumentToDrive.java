package com.momo.toys.be.service.impl;

import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.service.StoreDocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StoreDocumentToDrive implements StoreDocumentService {

    @Value("${document.photo.path}")
    private String filePath;

    @Override
    public void store(Document document) throws FileStorageException {
        Path path = Paths.get(String.format("%s\\%s", filePath, document.getFilename()));

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(document.getFileContent());
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException(e.getMessage());
        }
    }
}
