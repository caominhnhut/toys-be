package com.momo.toys.be.service;

import org.springframework.security.core.Authentication;

import com.momo.toys.be.model.Account;

import javassist.NotFoundException;

public interface AccountService{

    Long create(Account account);

    boolean update(Account account) throws NotFoundException;

    Authentication getAuthorizedAccount();
}
