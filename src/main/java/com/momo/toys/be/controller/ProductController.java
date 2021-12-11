package com.momo.toys.be.controller;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import com.momo.toys.be.entity.mongo.MenuCollection;
import com.momo.toys.be.entity.mongo.ProductCollection;
import com.momo.toys.be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController()
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/no-auth/products")
    public ResponseEntity findAll(){

        //List<ProductCollection> products = productRepository.findAll();

        List<MenuCollection> menuCollections = mock();

        return ResponseEntity.status(200).body(menuCollections);
    }

    @PostMapping("/no-auth/products")
    public ResponseEntity create(@RequestBody ProductCollection productCollection){

        productRepository.save(productCollection);

        return ResponseEntity.status(200).body(productCollection);
    }

    private List<MenuCollection> mock(){
        List<CategoryCollection> categoryCollections1 = new ArrayList<>();

        CategoryCollection categoryCollection1 = new CategoryCollection();
        categoryCollection1.setName("Đồ Chơi Bảng Tín");
        categoryCollections1.add(categoryCollection1);

        CategoryCollection categoryCollection2 = new CategoryCollection();
        categoryCollection2.setName("Đồ Chơi Hình Học");
        categoryCollections1.add(categoryCollection2);

        CategoryCollection categoryCollection3 = new CategoryCollection();
        categoryCollection3.setName("Đồ Chơi Con Vật");
        categoryCollections1.add(categoryCollection3);

        CategoryCollection categoryCollection4 = new CategoryCollection();
        categoryCollection4.setName("Đồ Chơi Học Tập");
        categoryCollections1.add(categoryCollection4);

        MenuCollection menuCollection = new MenuCollection();
        menuCollection.setName("Đồ Chơi Gỗ");
        menuCollection.getCategories().addAll(categoryCollections1);

        List<CategoryCollection> categoryCollections2 = new ArrayList<>();

        CategoryCollection categoryCollection21 = new CategoryCollection();
        categoryCollection21.setName("Xe Điều Khiển");
        categoryCollections2.add(categoryCollection21);

        CategoryCollection categoryCollection22 = new CategoryCollection();
        categoryCollection22.setName("Xe Địa Hình");
        categoryCollections2.add(categoryCollection22);

        CategoryCollection categoryCollection23 = new CategoryCollection();
        categoryCollection23.setName("Xe Công Trường");
        categoryCollections2.add(categoryCollection23);

        CategoryCollection categoryCollection24 = new CategoryCollection();
        categoryCollection24.setName("Dụng Cụ Làm Bếp");
        categoryCollections2.add(categoryCollection24);

        CategoryCollection categoryCollection25 = new CategoryCollection();
        categoryCollection25.setName("Máy Bay");
        categoryCollections2.add(categoryCollection25);

        CategoryCollection categoryCollection26 = new CategoryCollection();
        categoryCollection26.setName("Đồ Chơi Xúc Cát");
        categoryCollections2.add(categoryCollection26);

        MenuCollection menuCollection2 = new MenuCollection();
        menuCollection2.setName("Đồ Chơi Nhựa");
        menuCollection2.getCategories().addAll(categoryCollections2);

        List<MenuCollection> menues = Arrays.asList(menuCollection, menuCollection2);
        return menues;
    }
}
