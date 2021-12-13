package com.momo.toys.be.service;

import java.util.List;

import com.momo.toys.be.model.Account;
import com.momo.toys.be.model.Authority;

import javassist.NotFoundException;

public interface AccountService{

    Long create(Account account);
    boolean update(Account account) throws NotFoundException;
}
