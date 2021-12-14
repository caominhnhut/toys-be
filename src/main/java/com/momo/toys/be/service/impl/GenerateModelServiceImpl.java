package com.momo.toys.be.service.impl;

import com.momo.toys.be.product.EnumColor;
import com.momo.toys.be.product.EnumTag;
import com.momo.toys.be.product.Product;
import com.momo.toys.be.service.GenerateModelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Supplier;

@Service
public class GenerateModelServiceImpl implements GenerateModelService {

    @Override
    public Product generateProduct() {
        return mock.get();
    }

    private Supplier<Product> mock = () -> {
        Product product = new Product();
        product.setCode("W001");
        product.setName("Xe Kéo Hình Khối");
        product.setDescription("Sản phẩm chất lượng tuyệt vời");
        product.setAmount(7);
        product.setImageName("c.png");
        product.setCostPrice(BigDecimal.valueOf(100000));
        product.setPrice(BigDecimal.valueOf(150000));
        product.setColor(EnumColor.YELLOW);
        product.setTags(Arrays.asList(EnumTag.NEW));

        return product;
    };
}
