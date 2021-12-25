package com.momo.toys.be.service.impl;

import java.util.Calendar;
import java.util.function.UnaryOperator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.repository.DocumentRepository;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.service.StoreDocumentService;

@Service
public class DocumentServiceImpl implements DocumentService{

    @Value("${document.download.uri}")
    private String downloadUri;

    @Autowired
    private StoreDocumentService storeDocumentService;

    @Autowired
    private DocumentRepository documentRepository;

    private UnaryOperator<String> buildDownloadUri = filename -> downloadUri.concat(filename);

    @Override
    public Document upload(Document document){

        try{
            storeDocumentService.store(document);
        }catch(FileStorageException e){
            throw new IllegalArgumentException(e.getMessage());
        }

        document.setFileUri(buildDownloadUri.apply(document.getFilename()));
        DocumentEntity documentEntity = DocumentMapper.mapToDocumentEntity.apply(document);

        documentRepository.save(documentEntity);

        document.setId(documentEntity.getId());
        return document;
    }

    @Override
    public Document upload(Document document, ProductEntity product){
        String fileExtension = StringUtils.getFilenameExtension(document.getFilename());
        Long timeStamp = Calendar.getInstance().getTime().getTime();
        document.setFilename(String.format("%s.%s", timeStamp, fileExtension));

        try{
            storeDocumentService.store(document);
        }catch(FileStorageException e){
            throw new IllegalArgumentException(e.getMessage());
        }

        document.setFileUri(buildDownloadUri.apply(document.getFilename()));
        DocumentEntity documentEntity = DocumentMapper.mapToDocumentEntity.apply(document);
        documentEntity.setProduct(product);

        documentRepository.save(documentEntity);

        document.setId(documentEntity.getId());
        return document;
    }

    @Override
    public Document readFile(String filename) throws FileStorageException{

        byte[] content = storeDocumentService.load(filename);
        Document document = new Document();
        document.setFileContent(content);

        return document;
    }
}
