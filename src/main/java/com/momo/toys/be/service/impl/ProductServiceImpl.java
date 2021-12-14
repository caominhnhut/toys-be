package com.momo.toys.be.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

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
    public String create(String categoryId, Product product) throws NotFoundException {

        Optional<CategoryCollection> optionalCategoryCollection = categoryRepository.findById(categoryId);
        if (!optionalCategoryCollection.isPresent()) {
            throw new NotFoundException("The category not found");
        }

        Authentication authentication =accountService.getAuthenticatedUser();
        product.setOwner(authentication.getName());

        List<Document> insertedImages = insertImages.apply(product.getImages());

        ProductCollection productCollection = ProductMapper.mapToProductCollection.apply(product);

        List<DocumentMeta> documentMetas = convertToDocumentMeta.apply(product.getImageName(), insertedImages);
        productCollection.getImages().clear();
        productCollection.getImages().addAll(documentMetas);

        productRepository.save(productCollection);

        CategoryCollection category = optionalCategoryCollection.get();
        category.getProducts().add(productCollection);
        categoryRepository.save(category);

        return productCollection.getId();
    }

    private UnaryOperator<List<Document>> insertImages = documents -> {
        return documents.stream().map(document -> documentService.upload(document)).collect(Collectors.toList());
    };

    private BiFunction<String, List<Document>, List<DocumentMeta>> convertToDocumentMeta = (filename, documents) -> {
        return documents.stream().map(document -> {

            DocumentMeta documentMeta = new DocumentMeta();
            documentMeta.setUri(document.getFileUri());
            if (document.getFilename().equals(filename)) {
                documentMeta.setRequired(true);
            }

            return documentMeta;
        }).collect(Collectors.toList());
    };

}
