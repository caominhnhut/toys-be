package com.momo.toys.be.service;

import java.util.List;

import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.model.Category;

public interface CategoryService{

    List<Category> findByStatus(EntityStatus entityStatus);
}
