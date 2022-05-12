package com.momo.toys.be.controller;


import com.momo.toys.be.account.Problem;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.NavigationMapper;
import com.momo.toys.be.navigation.Navigation;
import com.momo.toys.be.navigation.NavigationId;
import com.momo.toys.be.service.NavigationService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

import static com.momo.toys.be.enumeration.SupportedType.NAVIGATION_CREATION;

@RestController
public class NavigationController {

    @Autowired
    private ValidationProvider validationProvider;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private NavigationService navigationService;

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
}
