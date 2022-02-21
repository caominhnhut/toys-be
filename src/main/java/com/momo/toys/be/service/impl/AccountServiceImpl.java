package com.momo.toys.be.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.Authority;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.AuthorityName;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.TokenHelper;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.model.Account;
import com.momo.toys.be.repository.AuthorityRepository;
import com.momo.toys.be.repository.UserRepository;
import com.momo.toys.be.service.AccountService;

import javassist.NotFoundException;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenHelper tokenHelper;

    @Override
    public Long create(Account account){

        UserEntity userEntity = AccountMapper.mapToEntity.apply(account);

        List<Authority> authorities = findAuthoritiesByName.apply(userEntity.getRoles());
        userEntity.setAuthorities(authorities);

        String encodedPassword = commonUtility.passwordEncoder().encode(account.getPassword());
        userEntity.setPassword(encodedPassword);

        UserEntity createdUser = userRepository.save(userEntity);

        return createdUser.getId();
    }

    @Override
    public boolean update(Account account) throws NotFoundException{
        List<Authority> authorities = findAuthoritiesByName.apply(AccountMapper.mapToAuthorityEntity.apply(account.getAuthorities()));
        Optional<UserEntity> optionalUserEntity = userRepository.findById(account.getId());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            userEntity.setAuthorities(authorities);
            try {
                userRepository.save(userEntity);
                return true;
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        throw new NotFoundException(String.format("Account with id [%s] not found", account.getId()));
    }

    @Override
    public Authentication getAuthenticatedUser(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean validToken(String token, HttpServletRequest request){
        return tokenHelper.validateToken(token, request);
    }

    private UnaryOperator<List<Authority>> findAuthoritiesByName = authorities -> {

        List<Authority> result = authorities.stream().map(authority -> {
            List<Authority> subAuthority = authorityRepository.findByName(authority.getName());
            return subAuthority.get(0);
        }).collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException(String.format("The role [%s] not found", AuthorityName.ROLE_CUSTOMER.name()));
        }

        return result;
    };
}
