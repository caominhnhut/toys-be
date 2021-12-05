package com.momo.toys.be.validation.impl;

import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_CREATION;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.momo.toys.be.account.Role;
import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;

@Component
public class RolesValidator implements Validator{

    private static final String ERROR_MESSAGE = "The roles should not be empty";

    @Override
    public void validate(ValidationData data) throws ValidationException{

        isRolesNotEmpty(data.getRoles());
    }

    @Override
    public List<SupportedType> getSupportedTypes(){
        return Arrays.asList(ACCOUNT_CREATION);
    }

    private void isRolesNotEmpty(List<Role> roles) throws ValidationException{
        if(roles.isEmpty()){
            throw new ValidationException(ERROR_MESSAGE);
        }
    }
}
