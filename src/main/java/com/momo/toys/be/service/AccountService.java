package com.momo.toys.be.service;

import com.momo.toys.be.model.Account;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;

public interface AccountService {

    Long create(Account account);

    boolean update(Account account) throws NotFoundException;

    Authentication getAuthenticatedUser();
}
