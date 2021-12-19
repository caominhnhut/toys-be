package com.momo.toys.be.validation.impl;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class DocumentValidator implements Validator {

    @Override
    public void validate(ValidationData data) throws ValidationException {

        if(data.getMultipartFile() == null){
            throw new ValidationException("The file is not present");
        }

        String filename = StringUtils.cleanPath(data.getMultipartFile().getOriginalFilename());
        //TODO: check filename should not contain any special character
        if(filename.contains("..")){
            throw new ValidationException("Filename shouldn't contain any special character");
        }
    }

    @Override
    public List<SupportedType> getSupportedTypes() {
        return Arrays.asList(SupportedType.DOCUMENT_UPLOADING);
    }
}
