package com.momo.toys.be.service;

import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.model.Navigation;
import javassist.NotFoundException;

public interface NavigationService {
    Long create(Navigation navigation);
    NavigationEntity findById(Long id) throws NotFoundException;

}
