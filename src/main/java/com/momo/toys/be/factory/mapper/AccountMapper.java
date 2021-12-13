package com.momo.toys.be.factory.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.momo.toys.be.account.Account;
import com.momo.toys.be.account.Role;
import com.momo.toys.be.entity.Authority;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.AuthorityName;

public class AccountMapper{

    public static Function<List<Role>, List<com.momo.toys.be.model.Authority>> mapToAuthorityList = roles -> roles.stream().map(role -> {
        com.momo.toys.be.model.Authority authority = new com.momo.toys.be.model.Authority();
        authority.setAuthorityName(AuthorityName.valueOf(role.toString()));
        return authority;
    }).collect(Collectors.toList());

    public static final Function<Account, com.momo.toys.be.model.Account> mapToModel = dto -> {

        com.momo.toys.be.model.Account model = new com.momo.toys.be.model.Account();
        model.setUserName(dto.getUserName());
        model.setPassword(dto.getPassword());
        model.getAuthorities().addAll(mapToAuthorityList.apply(dto.getRoles()));
        return model;
    };

    public static Function<List<com.momo.toys.be.model.Authority>, List<Authority>> mapToAuthorityEntity = authorities -> authorities.stream().map(authority -> {
        Authority authorityEntity = new Authority();
        authorityEntity.setName(authority.getAuthorityName());
        return authorityEntity;
    }).collect(Collectors.toList());

    public static final Function<com.momo.toys.be.model.Account, UserEntity> mapToEntity = account -> {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(account.getUserName());
        userEntity.setPassword(account.getPassword());
        userEntity.setAuthorities(mapToAuthorityEntity.apply(account.getAuthorities()));

        return userEntity;
    };

    private AccountMapper(){
        // hide constructor
    }
}
