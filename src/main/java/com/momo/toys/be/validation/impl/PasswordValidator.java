package com.momo.toys.be.validation.impl;

import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_CREATION;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;

@Component
public class PasswordValidator implements Validator{

    private static final Pattern VALID_PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    private static final String ERROR_MESSAGE = "Password must have Uppercase, lowercase, number and special character";

    @Override
    public void validate(ValidationData data) throws ValidationException{
        isNotEmpty(data.getPassword());
        isCorrectFormat(data.getPassword());
    }

    @Override
    public List<SupportedType> getSupportedTypes(){
        return Arrays.asList(ACCOUNT_CREATION);
    }

    private void isNotEmpty(String password) throws ValidationException{
        if(Strings.isEmpty(password)){
            throw new ValidationException("Password should not be empty");
        }
    }

    private void isCorrectFormat(String password) throws ValidationException{

        Matcher matcher = VALID_PASSWORD.matcher(password);
        boolean isMatch = matcher.find();

        if(!isMatch){
            throw new ValidationException(ERROR_MESSAGE);
        }
    }
}
