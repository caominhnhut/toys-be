package com.momo.toys.be.service.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.Authority;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.AuthorityName;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.model.Account;
import com.momo.toys.be.repository.AuthorityRepository;
import com.momo.toys.be.repository.UserRepository;
import com.momo.toys.be.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

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

    private Function<List<Authority>, List<Authority>> findAuthoritiesByName = authorities -> {

        List<Authority> result = authorities.stream().map(authority -> {
            List<Authority> subAuthority = authorityRepository.findByName(authority.getName());
            return subAuthority.get(0);
        }).collect(Collectors.toList());

        if(result.isEmpty()){
            throw new IllegalArgumentException(String.format("The role [%s] not found", AuthorityName.ROLE_CUSTOMER.name()));
        }

        return result;
    };
}
