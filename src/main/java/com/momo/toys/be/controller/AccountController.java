package com.momo.toys.be.controller;

import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_CREATION;

import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.account.Account;
import com.momo.toys.be.account.AccountId;
import com.momo.toys.be.account.AuthenticatedResult;
import com.momo.toys.be.account.Credential;
import com.momo.toys.be.account.Problem;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.TokenHelper;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;

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

    @Autowired
    private ValidationProvider validationProvider;

    private static final String AUTHENTICATION_ERROR = "Username or password is incorrect";

    @PostMapping("/no-auth/account")
    public ResponseEntity createAccount(@RequestBody Account accountDto){

        Problem problem = validatorAccountCreation.apply(accountDto);
        if(Strings.isNotEmpty(problem.getTitle())){
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
        }catch(AuthenticationException e){
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

    @PutMapping("/account/{accountId}")
    public ResponseEntity updateRoles(@RequestBody List<String> roles){

        return null;
    }

    private Function<Account, Problem> validatorAccountCreation = accountDto -> {

        Problem problem = new Problem();

        ValidationData validationData = new ValidationData().setEmail(accountDto.getUserName()).setPassword(accountDto.getPassword()).setRoles(accountDto.getRoles());

        try{
            validationProvider.executeValidators(validationData, ACCOUNT_CREATION);
        }catch(ValidationException e){
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return problem;
    };
}
