package com.momo.toys.be.controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.dto.Account;
import com.momo.toys.be.dto.AccountId;
import com.momo.toys.be.dto.Problem;
import com.momo.toys.be.dto.Role;
import com.momo.toys.be.enumeration.FieldName;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.service.AccountService;

/*
    FE ----> DTO(Account) BE (Controller)
    Controller: Recive requestion, return Response
    Service: Handle logic
    Repository: DB
    ---
    FE ----> DTO (target) <---- BE
    Controller ----> Model <----- Service
    Service ----> Entity <------ Repository
     */

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    private Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    private Predicate<String> validateEmailFormat = emailStr -> {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    };

    private Predicate<String> validatePassword = pwStr -> {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pwStr);
        return matcher.find();
    };

    @PostMapping("/authenticate/account")
    private ResponseEntity createAccount(@RequestBody Account accountDto){

        try{
            validate(accountDto);
        }catch(ValidationException e){

            Problem problem = new Problem();
            problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            problem.setDetail(e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.momo.toys.be.model.Account accountModel = AccountMapper.mapToModel.apply(accountDto);

        Long id = accountService.create(accountModel);

        AccountId accountId = new AccountId();
        accountId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    private void validate(Account account) throws ValidationException{

        isNotEmpty(account.getUserName(), FieldName.userName);

        isNotEmpty(account.getPassword(), FieldName.password);

        isRolesNotEmpty(account.getRoles());

        validateUserName(account.getUserName(), FieldName.userName);

        validatePassword(account.getPassword(), FieldName.password);
    }

    private void isNotEmpty(String data, FieldName fieldName) throws ValidationException{
        if(data.length() == 0){
            throw new ValidationException(fieldName.getEmptyMessage());
        }
    }

    private void isRolesNotEmpty(List<Role> roles) throws ValidationException{
        if(roles.isEmpty()){
            throw new ValidationException(FieldName.roles.getEmptyMessage());
        }
    }

    private void validateUserName(String userName, FieldName fieldName) throws ValidationException{
        if(!validateEmailFormat.test(userName)){
            throw new ValidationException(fieldName.userName.getValidationMessage());
        }
    }

    private void validatePassword(String password, FieldName fieldName) throws ValidationException{
        if(!validatePassword.test(password)){
            throw new ValidationException(fieldName.password.getValidationMessage());
        }
    }
}
