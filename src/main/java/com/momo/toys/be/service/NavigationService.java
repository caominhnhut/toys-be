package com.momo.toys.be.service;

import java.util.List;

import com.momo.toys.be.model.Navigation;

public interface NavigationService {
    Long create(Navigation navigation);

    List<Navigation> findAll();
}
