package com.momo.toys.be.validation.impl;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.momo.toys.be.enumeration.SupportedType.ORDER_CREATION;

@Component
public class OrderValidator implements Validator {

    @Override
    public void validate(ValidationData data) throws ValidationException {
        isQuantityNotEmpty(data.getQuantityProduct());

    }

    private void isQuantityNotEmpty(Integer quantity) throws ValidationException {
        if (quantity == 0) {
            throw new ValidationException("The quantity of products should not be zero");
        }
    }


    @Override
    public List<SupportedType> getSupportedTypes() {
        return Arrays.asList(ORDER_CREATION);

    }
}
