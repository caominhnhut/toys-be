package com.momo.toys.be.validation.impl;

import static com.momo.toys.be.enumeration.SupportedType.NAVIGATION_CREATION;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;

@Component
public class NavigationNameValidator implements Validator {

    @Override
    public void validate(ValidationData data) throws ValidationException {
        isNavigationNameNotEmpty(data.getNavigationName());
    }

    @Override
    public List<SupportedType> getSupportedTypes() {
        return Arrays.asList(NAVIGATION_CREATION);
    }

    private void isNavigationNameNotEmpty(String navigationName) throws ValidationException{
        if(Strings.isEmpty(navigationName)){
            throw new ValidationException("Navigation name should not empty");
        }
    }
}
