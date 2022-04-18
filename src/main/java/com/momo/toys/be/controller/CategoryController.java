package com.momo.toys.be.controller;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.dto.CategoryDto;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.CategoryMapper;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.service.CategoryService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommonUtility commonUtility;

    @PostMapping("/navigation/{navigation-id}/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity create(@PathVariable("navigation-id") Long navigationId, @RequestBody CategoryDto categoryDto) {

        Category categoryModel = CategoryMapper.mapToModel.apply(categoryDto);
        categoryModel.setNavigationId(navigationId);

        try {
            Long id = categoryService.create(categoryModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (NotFoundException e) {
           Problem problem = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);

        }

    }

}
