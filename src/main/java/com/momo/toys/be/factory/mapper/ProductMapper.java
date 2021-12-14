package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.entity.mongo.ProductCollection;
import com.momo.toys.be.enumeration.EnumColor;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.product.EnumTag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ProductMapper {

    private ProductMapper() {
        // hide constructor
    }

    private static BiConsumer<List<EnumTag>, Product> setProductTags = (tagsDto, product) -> {

        if (tagsDto.isEmpty()) {
            return;
        }

        for (EnumTag tagDto : tagsDto) {
            product.getTags().add(com.momo.toys.be.enumeration.EnumTag.valueOf(tagDto.toString()));
        }
    };

    public static Function<com.momo.toys.be.product.Product, Product> mapToProductModel = productDto -> {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setCode(productDto.getCode());
        product.setOwner(productDto.getOnwer());
        product.setDescription(productDto.getDescription());
        product.setImageName(productDto.getImageName());
        product.setAmount(productDto.getAmount());
        product.setCostPrice(productDto.getCostPrice());
        product.setPrice(productDto.getPrice());
        product.setColor(EnumColor.valueOf(productDto.getColor().toString()));

        setProductTags.accept(productDto.getTags(), product);

        return product;
    };

    public static BiConsumer<MultipartFile[], Product> mapImages = (images, product) -> {
        for (MultipartFile image : images) {
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
        productCollection.setColor(product.getColor());
        productCollection.getTags().addAll(product.getTags());

        return productCollection;
    };
}
