package com.momo.toys.be.validation.impl;

import static com.momo.toys.be.enumeration.SupportedType.CATEGORY_CREATION;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;

@Component
public class CategoryNameValidator implements Validator {

    @Override
    public void validate(ValidationData data) throws ValidationException {
        if(Strings.isEmpty(data.getCategoryName())){
            throw new ValidationException("Category name should not empty");
        }
    }

    @Override
    public List<SupportedType> getSupportedTypes() {
        return Arrays.asList(CATEGORY_CREATION);
    }
}
