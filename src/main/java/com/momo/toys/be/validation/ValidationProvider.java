package com.momo.toys.be.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;

@Component
public class ValidationProvider{

    @Autowired
    private List<Validator> validators;

    public void executeValidators(ValidationData data, SupportedType supportedType) throws ValidationException{
        for(Validator validator: validators){
            if(validator.getSupportedTypes().contains(supportedType)){
                validator.validate(data);
            }
        }
    }
}
