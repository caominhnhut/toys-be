package com.momo.toys.be.service;

import com.momo.toys.be.dto.AccountDto;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.model.Account;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {

    Long create(Account account);

    boolean update(Account account) throws NotFoundException;

    Authentication getAuthorizedAccount();

    List<UserEntity> findAll() throws NotFoundException;
    List<UserEntity> findStatus(String status) throws NotFoundException;

    boolean updateStatus(AccountDto accountDto) throws NotFoundException;

    boolean delete(Long id) throws NotFoundException;

    UserEntity findUserByName(String name);

}
