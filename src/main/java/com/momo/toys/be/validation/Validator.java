package com.momo.toys.be.validation;

import java.util.List;

import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;

public interface Validator{

    void validate(ValidationData data) throws ValidationException;

    List<SupportedType> getSupportedTypes();
}
