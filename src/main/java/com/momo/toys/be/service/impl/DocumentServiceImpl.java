package com.momo.toys.be.service.impl;

import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.repository.DocumentRepository;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.service.StoreDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Value("${document.download.uri}")
    private String downloadUri;

    @Autowired
    private StoreDocumentService storeDocumentService;

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document upload(Document document) {

        try {
            storeDocumentService.store(document);
        } catch (FileStorageException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        document.setFileUri(buildDownloadUri.apply(document.getFilename()));
        DocumentEntity documentEntity = DocumentMapper.mapToDocumentEntity.apply(document);

        documentRepository.save(documentEntity);

        document.setId(documentEntity.getId());
        return document;
    }

    private UnaryOperator<String> buildDownloadUri = filename -> downloadUri.concat(filename);
}
