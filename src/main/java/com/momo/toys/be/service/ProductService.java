package com.momo.toys.be.service;

import java.util.List;

import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;

public interface ProductService{
    Long create(Product product, List<Document> images);
}
