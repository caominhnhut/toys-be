package com.momo.toys.be.validation.impl;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.momo.toys.be.enumeration.SupportedType.ORDER_VERIFY_AMOUNT;

@Component
public class OrderProductValidator implements Validator {

    @Override
    public void validate(ValidationData data) throws ValidationException {
        isAmountNotEnough(data.getQuantityProduct(), data.getAmountProduct());

    }

    private void isAmountNotEnough(Integer quantity, Integer amount) throws ValidationException {
        if (amount < quantity) {
            throw new ValidationException("The amount of product is not enough");
        }
    }


    @Override
    public List<SupportedType> getSupportedTypes() {
        return Arrays.asList(ORDER_VERIFY_AMOUNT);

    }
}



