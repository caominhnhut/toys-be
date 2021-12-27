package com.momo.toys.be.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.mongo.MenuCollection;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.factory.mapper.MenuMapper;
import com.momo.toys.be.model.Menu;
import com.momo.toys.be.repository.MenuRepository;
import com.momo.toys.be.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findAll(){
        List<MenuCollection> menus = menuRepository.findByStatus(EntityStatus.ACTIVE.toString());
        return menus.stream().map(MenuMapper.mapFromCollection::apply).collect(Collectors.toList());
    }
}
