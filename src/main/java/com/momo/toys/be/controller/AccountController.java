package com.momo.toys.be.controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.dto.Account;
import com.momo.toys.be.dto.AccountId;
import com.momo.toys.be.dto.AuthenticatedResult;
import com.momo.toys.be.dto.Credential;
import com.momo.toys.be.dto.Problem;
import com.momo.toys.be.dto.Role;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.FieldName;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.TokenHelper;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.service.AccountService;

@RestController
public class AccountController{

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private CommonUtility commonUtility;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    private static final String AUTHENTICATION_ERROR = "Username or password is incorrect";

    private Predicate<String> validateEmailFormat = emailStr -> {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    };

    private Predicate<String> validatePassword = pwStr -> {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pwStr);
        return matcher.find();
    };

    @PostMapping("/no-auth/account")
    public ResponseEntity createAccount(@RequestBody Account accountDto){

        try{
            validate(accountDto);
        }catch(ValidationException e){
            Problem problem = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.momo.toys.be.model.Account accountModel = AccountMapper.mapToModel.apply(accountDto);

        Long id = accountService.create(accountModel);

        AccountId accountId = new AccountId();
        accountId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    @PostMapping("/authenticate")
    public ResponseEntity login(@RequestBody Credential credential){

        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credential.getUserName(), credential.getPassword()));
        }
        catch(AuthenticationException e){
            Problem problem = commonUtility.createProblem(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED.value(), AUTHENTICATION_ERROR);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        final String token = tokenHelper.generateToken(user.getUsername());

        AuthenticatedResult response = new AuthenticatedResult();
        response.setToken(token);
        response.setExpiredIn(tokenHelper.getExpiresIn());
        return ResponseEntity.ok(response);
    }

    private void validate(Account account) throws ValidationException{

        isNotEmpty(account.getUserName(), FieldName.userName);

        isNotEmpty(account.getPassword(), FieldName.password);

        isRolesNotEmpty(account.getRoles());

        validateUserName(account.getUserName());

        validatePassword(account.getPassword());
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

    private void validateUserName(String userName) throws ValidationException{
        if(!validateEmailFormat.test(userName)){
            throw new ValidationException(FieldName.userName.getValidationMessage());
        }
    }

    private void validatePassword(String password) throws ValidationException{
        if(!validatePassword.test(password)){
            throw new ValidationException(FieldName.password.getValidationMessage());
        }
    }
}
