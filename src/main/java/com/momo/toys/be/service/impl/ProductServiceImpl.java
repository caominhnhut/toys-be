package com.momo.toys.be.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import com.momo.toys.be.entity.mongo.ProductCollection;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.DocumentMeta;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.repository.ProductRepository;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.service.ProductService;

import javassist.NotFoundException;

@Service
public class ProductServiceImpl implements ProductService{

    private static final String CATEGORY_NOT_FOUND = "The category not found";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    @Qualifier("mongoDocumentService")
    private DocumentService documentService;

    @Autowired
    private AccountService accountService;

    @Override
    public String create(String categoryId, Product product) throws NotFoundException{

        Optional<CategoryCollection> optionalCategoryCollection = categoryRepository.findById(categoryId);
        if (!optionalCategoryCollection.isPresent()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        Authentication authentication = accountService.getAuthenticatedUser();
        product.setOwner(authentication.getName());

        ProductCollection productCollection = ProductMapper.mapToProductCollection.apply(product);
        if (!product.getImages().isEmpty()) {
            List<Document> insertedImages = insertImages.apply(product.getImages());
            List<DocumentMeta> documentMetas = convertToDocumentMeta(insertedImages);
            productCollection.setImages(documentMetas);
        }

        productRepository.save(productCollection);

        CategoryCollection category = optionalCategoryCollection.get();
        category.getProducts().add(productCollection);
        categoryRepository.save(category);

        return productCollection.getId();
    }

    @Override
    public List<Product> findByCategoryId(String categoryId) throws NotFoundException{

        Optional<CategoryCollection> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryCollection category = optionalCategory.get();

        List<ProductCollection> products = category.getProducts();

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream().map(ProductMapper.mapFromProductCollection).collect(Collectors.toList());
    }

    private UnaryOperator<List<Document>> insertImages = documents -> documents.stream().map(document -> documentService.upload(document)).collect(Collectors.toList());

    private List<DocumentMeta> convertToDocumentMeta(List<Document> documents){
        List<DocumentMeta> documentMetaList = new ArrayList<>();
        for (int i = 0; i < documents.size(); i++) {
            Document document = documents.get(i);
            DocumentMeta documentMeta = new DocumentMeta();
            documentMeta.setUrl(document.getDocumentUrl());
            documentMeta.setDocumentId(document.getObjectId());
            if (i == 0) {
                documentMeta.setRequired(true);
            }
            documentMetaList.add(documentMeta);
        }

        return documentMetaList;
    }
}
