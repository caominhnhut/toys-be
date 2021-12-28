package com.momo.toys.be.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.factory.CommonUtility;

@ControllerAdvice
public class ToyExceptionHandler{

    @Autowired
    private CommonUtility commonUtility;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> conflict(DataIntegrityViolationException e){

        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        Problem problem = commonUtility.createProblem(HttpStatus.CONFLICT.toString(), HttpStatus.CONFLICT.value(), message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}
