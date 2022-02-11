package com.momo.toys.be.factory.mapper;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.momo.toys.be.entity.mongo.ProductCollection;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.product.EnumTag;
import com.momo.toys.be.product.FileData;

public class ProductMapper{

    private ProductMapper(){
        // hide constructor
    }

    private static BiConsumer<List<EnumTag>, Product> mapToProductModelTags = (tagsDto, product) -> {

        if(tagsDto.isEmpty()){
            return;
        }

        for(EnumTag tagDto : tagsDto){
            product.getTags().add(com.momo.toys.be.enumeration.EnumTag.valueOf(tagDto.toString()));
        }
    };

    private static BiConsumer<List<com.momo.toys.be.enumeration.EnumTag>, com.momo.toys.be.product.Product> mapToProductDtoTags = (tagsModel, product) -> {

        if(tagsModel.isEmpty()){
            return;
        }

        for(com.momo.toys.be.enumeration.EnumTag tagModel : tagsModel){
            product.getTags().add(EnumTag.valueOf(tagModel.toString()));
        }
    };

    public static Function<com.momo.toys.be.product.Product, Product> mapToProductModel = productDto -> {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCode(productDto.getCode());
        product.setOwner(productDto.getOnwer());
        product.setDescription(productDto.getDescription());
        product.setAmount(productDto.getAmount());
        product.setCostPrice(productDto.getCostPrice());
        product.setPrice(productDto.getPrice());

        mapToProductModelTags.accept(productDto.getTags(), product);

        return product;
    };

    public static BiConsumer<MultipartFile[], Product> mapImages = (images, product) -> {
        for(MultipartFile image : images){
            Document document = DocumentMapper.mapToDocument.apply(image);
            product.getImages().add(document);
        }
    };

    public static Function<Product, ProductCollection> mapToProductCollection = product -> {

        ProductCollection productCollection = new ProductCollection();
        productCollection.setCode(product.getCode());
        productCollection.setName(product.getName());
        productCollection.setOwner(product.getOwner());
        productCollection.setDescription(product.getDescription());
        productCollection.setAmount(product.getAmount());
        productCollection.setCostPrice(product.getCostPrice());
        productCollection.setPrice(product.getPrice());
        productCollection.getTags().addAll(product.getTags());

        return productCollection;
    };

    public static Function<ProductCollection, Product> mapFromProductCollection = productCollection -> {

        Product product = new Product();
        product.setId(productCollection.getId());
        product.setCode(productCollection.getCode());
        product.setName(productCollection.getName());
        product.setOwner(productCollection.getOwner());
        product.setDescription(productCollection.getDescription());
        product.setAmount(productCollection.getAmount());
        product.setCostPrice(productCollection.getCostPrice());
        product.setPrice(productCollection.getPrice());
        product.getTags().addAll(productCollection.getTags());

        List<Document> documents = productCollection.getImages().stream().map(image -> {
            Document document = new Document();
            document.setRequired(image.isRequired());
            document.setDocumentUrl(image.getUrl());
            return document;
        }).collect(Collectors.toList());

        product.getImages().addAll(documents);

        return product;
    };

    public static Function<Product, com.momo.toys.be.product.Product> mapToProductDto = product -> {
        com.momo.toys.be.product.Product productDto = new com.momo.toys.be.product.Product();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setCode(product.getCode());
        productDto.setOnwer(product.getOwner());
        productDto.setDescription(product.getDescription());
        productDto.setAmount(product.getAmount());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setPrice(product.getPrice());

        mapToProductDtoTags.accept(product.getTags(), productDto);

        List<FileData> filesData = product.getImages().stream().map(image -> {
            FileData fileData = new FileData();
            fileData.setIsMainImage(image.isRequired());
            fileData.setFileUri(image.getDocumentUrl());
            return fileData;
        }).collect(Collectors.toList());

        productDto.getImages().addAll(filesData);

        return productDto;
    };
}
