package com.momo.toys.be.controller;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.dto.CategoryDto;
import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.CategoryMapper;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.service.CategoryService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/no-auth/categories")
    public ResponseEntity getCategories(@RequestParam(required = false, name = "offset") Integer offset, @RequestParam(required = false, name = "limit") Integer limit) throws NotFoundException {

        List<CategoryEntity> category;

        if (offset == null || limit == null) {
            category = categoryService.findAll();

        } else {
            category = categoryService.findCategories(limit, offset);
        }
        List<CategoryDto> categoriesDto = category.stream().map(CategoryMapper.mapToDto).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(categoriesDto);
    }

    @PutMapping("/categories/{category-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateCategory(@PathVariable("category-id") Long categoryId, @RequestBody CategoryDto categoryDto) throws NotFoundException {

        categoryDto.setId(categoryId);
        categoryService.updateCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Category with id [%s] updated", categoryId));
    }

    @DeleteMapping("/categories/{category-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteCategory(@PathVariable("category-id") Long categoryId) throws NotFoundException{

        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.GONE).body(String.format("Category with id [%s] is deleted", categoryId));
    }
}
