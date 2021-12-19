package com.momo.toys.be.validation.impl;

import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_CREATION;
import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_UPDATING;
import static com.momo.toys.be.enumeration.SupportedType.PRODUCT_CREATION;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.momo.toys.be.account.Role;
import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;

@Component
public class ProductValidator implements Validator{

    @Override
    public void validate(ValidationData data) throws ValidationException{
        isNameNotEmpty(data.getProductName());
        isCodeNotEmpty(data.getProductCode());
    }

    private void isNameNotEmpty(String name) throws ValidationException{
        if(name.isEmpty()){
            throw new ValidationException("The product name should not be empty");
        }
    }

    private void isCodeNotEmpty(String code) throws ValidationException{
        if(code.isEmpty()){
            throw new ValidationException("The product code should not be empty");
        }
    }

    @Override
    public List<SupportedType> getSupportedTypes(){
        return Arrays.asList(PRODUCT_CREATION);
    }
}
