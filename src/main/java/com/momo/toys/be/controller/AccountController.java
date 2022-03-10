package com.momo.toys.be.controller;

import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_CREATION;
import static com.momo.toys.be.enumeration.SupportedType.ACCOUNT_UPDATING;
import static com.momo.toys.be.factory.ConstantUtility.AUTHORIZATION;
import static com.momo.toys.be.factory.ConstantUtility.BEARER;

import java.util.List;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.account.Account;
import com.momo.toys.be.account.AuthenticatedResult;
import com.momo.toys.be.account.Credential;
import com.momo.toys.be.account.Role;
import com.momo.toys.be.dto.Problem;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.TokenHelper;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.model.Authority;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;

import javassist.NotFoundException;

@RestController public class AccountController{

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

        Long accountId = accountService.create(accountModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountId);
    }

    @PostMapping("/authenticate")
    public ResponseEntity login(@RequestBody Credential credential){

        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credential.getUserName(), credential.getPassword()));
        }catch(AuthenticationException e){
            com.momo.toys.be.dto.Problem problem = commonUtility.createProblem(HttpStatus.UNAUTHORIZED.toString(), HttpStatus.UNAUTHORIZED.value(), AUTHENTICATION_ERROR);
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

    @PutMapping("/account/{account-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity updateRoles(@PathVariable("account-id") Long accountId, @RequestBody List<Role> roles) throws NotFoundException{
        com.momo.toys.be.dto.Problem problem = validatorAccountUpdating.apply(roles);
        if(Strings.isNotEmpty(problem.getTitle())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.momo.toys.be.model.Account account = new com.momo.toys.be.model.Account();
        account.setId(accountId);

        List<Authority> authorities = AccountMapper.mapToAuthorityList.apply(roles);
        account.setAuthorities(authorities);

        return ResponseEntity.status(HttpStatus.OK).body(accountService.update(account));

    }

    @GetMapping("/no-auth/account/token")
    public ResponseEntity validToken(HttpServletRequest request){
        final String header = request.getHeader(AUTHORIZATION);
        String token = header.substring(BEARER.length());
        return ResponseEntity.status(HttpStatus.OK).body(accountService.validToken(token, request));
    }

    private Function<Account, com.momo.toys.be.dto.Problem> validatorAccountCreation = accountDto -> {

        com.momo.toys.be.dto.Problem problem = new com.momo.toys.be.dto.Problem();

        ValidationData validationData = new ValidationData().setEmail(accountDto.getUserName()).setPassword(accountDto.getPassword()).setRoles(accountDto.getRoles());

        try{
            validationProvider.executeValidators(validationData, ACCOUNT_CREATION);
        }catch(ValidationException e){
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return problem;
    };

    private Function<List<Role>, com.momo.toys.be.dto.Problem> validatorAccountUpdating = roles -> {

        com.momo.toys.be.dto.Problem problem = new com.momo.toys.be.dto.Problem();

        ValidationData validationData = new ValidationData().setRoles(roles);

        try{
            validationProvider.executeValidators(validationData, ACCOUNT_UPDATING);
        }catch(ValidationException e){
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return problem;
    };
}
