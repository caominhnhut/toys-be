package com.momo.toys.be.service.impl;

import java.util.function.UnaryOperator;

import com.momo.toys.be.entity.ReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.factory.CommonUtility;
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

    @Autowired
    private CommonUtility commonUtility;

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
    public Document uploadComment(Document document, ReviewEntity review){

        try{
            storeDocumentService.store(document);
        }catch(FileStorageException e){
            throw new IllegalArgumentException(e.getMessage());
        }

        document.setFileUri(buildDownloadUri.apply(document.getFilename()));
        DocumentEntity documentEntity = DocumentMapper.mapToDocumentEntity.apply(document);
        documentEntity.setReview(review);

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

    @Override
    public void delete(Document document) throws FileStorageException{

        // Delete physical file
        storeDocumentService.delete(document);

        // Delete record in document table
        documentRepository.deleteById(document.getId());

    }
}
