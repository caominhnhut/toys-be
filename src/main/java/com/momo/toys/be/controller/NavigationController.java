package com.momo.toys.be.controller;

import static com.momo.toys.be.enumeration.SupportedType.CATEGORY_CREATION;
import static com.momo.toys.be.enumeration.SupportedType.NAVIGATION_CREATION;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.dto.CategoryDTO;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.CategoryMapper;
import com.momo.toys.be.factory.mapper.NavigationMapper;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.navigation.Navigation;
import com.momo.toys.be.navigation.NavigationId;
import com.momo.toys.be.service.CategoryService;
import com.momo.toys.be.service.NavigationService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;

import javassist.NotFoundException;

@RestController
public class NavigationController {

    @Autowired
    private ValidationProvider validationProvider;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private NavigationService navigationService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/navigation")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity create(@RequestBody Navigation navigationDto){
        Problem problem = validatorNavigationCreation.apply(navigationDto);
        if (Strings.isNotEmpty(problem.getTitle())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.momo.toys.be.model.Navigation navigationModel = NavigationMapper.mapToModel.apply(navigationDto);

        Long id = navigationService.create(navigationModel);

        NavigationId navigationId = new NavigationId();
        navigationId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(navigationId);
    }

    @PostMapping("/navigation/{navigation-id}/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity createCategory(@PathVariable("navigation-id") Long navigationId, @RequestBody CategoryDTO categoryDto){
        Problem problem = validatorCategoryCreation.apply(categoryDto);
        if (Strings.isNotEmpty(problem.getTitle())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        Category categoryModel = CategoryMapper.mapToModel.apply(categoryDto);
        categoryModel.setNavigationId(navigationId);

        try{
            Long categoryId = categoryService.create(categoryModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/no-auth/navigation/{navigation-id}/categories")
    public ResponseEntity getCategories(@PathVariable("navigation-id") Long navigationId){
        List<Category> categories = categoryService.getCategoryByNavigation(navigationId);
        if(categories == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/no-auth/navigation")
    public ResponseEntity<List<Navigation>> findAll(){

        List<com.momo.toys.be.model.Navigation> navigationModels = navigationService.findAll();

        if(navigationModels.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }

        List<Navigation> navigations = navigationModels.stream().map(NavigationMapper.mapFromModel).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(navigations);
    }

    private Function<Navigation, Problem> validatorNavigationCreation = navigationDto -> {

        Problem problem = new Problem();

        ValidationData validationData = new ValidationData().setNavigationName(navigationDto.getName());
        try {
            validationProvider.executeValidators(validationData, NAVIGATION_CREATION);
        } catch (ValidationException e) {
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return problem;
    };

    private Function<CategoryDTO, Problem> validatorCategoryCreation = categoryDTO -> {

        Problem problem = new Problem();

        ValidationData validationData = new ValidationData().setCategoryName(categoryDTO.getName());
        try {
            validationProvider.executeValidators(validationData, CATEGORY_CREATION);
        } catch (ValidationException e) {
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return problem;
    };
}
