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
public class UserNameValidator implements Validator{

    private static final Pattern EMAIL_FORMAT = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void validate(ValidationData data) throws ValidationException{
        isUserNameNotEmpty(data.getEmail());
        isCorrectEmailFormat(data.getEmail());
    }

    @Override
    public List<SupportedType> getSupportedTypes(){
        return Arrays.asList(ACCOUNT_CREATION);
    }

    private void isUserNameNotEmpty(String userName) throws ValidationException{
        if(Strings.isEmpty(userName)){
            throw new ValidationException("Username should not empty");
        }
    }

    private void isCorrectEmailFormat(String email) throws ValidationException{
        Matcher matcher = EMAIL_FORMAT.matcher(email);
        boolean isMatch = matcher.find();
        if(!isMatch){
            throw new ValidationException("The username should be correct with email format");
        }
    }
}
