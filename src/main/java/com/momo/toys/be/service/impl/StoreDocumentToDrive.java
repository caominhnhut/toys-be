package com.momo.toys.be.service.impl;

import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.service.StoreDocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StoreDocumentToDrive implements StoreDocumentService {

    @Value("${document.photo.path}")
    private String filePath;

    @Override
    public void store(Document document) throws FileStorageException {
        Path path = Paths.get(filePath.concat(document.getFilename()));
        System.out.println("==>"+path);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(document.getFileContent());
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException(e.getMessage());
        }
    }

    @Override
    public void delete(Document document) throws FileStorageException{
        Path path = Paths.get(filePath.concat(document.getFilename()));
        try{
            // Delete file
            Files.delete(path);
            System.out.println("File or directory deleted successfully");
        }catch(NoSuchFileException ex){
            System.out.printf("No such file or directory: %s\n", path);
        }catch(IOException ex){
            System.out.println(ex);
        }
    }

    @Override
    public byte[] load(String filename) throws FileStorageException {
        Path path = Paths.get(String.format("%s\\%s", filePath, filename));
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }
    }


}
