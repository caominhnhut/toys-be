package com.momo.toys.be.exception;

public class ValidationException extends Exception{

    public ValidationException(String errorMessage){
        super(errorMessage);
    }
}
