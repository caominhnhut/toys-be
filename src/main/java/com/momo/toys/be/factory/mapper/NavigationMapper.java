package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.account.Account;
import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.navigation.Navigation;

import java.util.function.Function;

public class NavigationMapper {
    public static final Function<Navigation, com.momo.toys.be.model.Navigation> mapToModel = dto -> {

        com.momo.toys.be.model.Navigation model = new com.momo.toys.be.model.Navigation();
        model.setName(dto.getName());
        return model;
    };

    public static final Function<com.momo.toys.be.model.Navigation, NavigationEntity> mapToEntity = navigation -> {
        NavigationEntity navigationEntity = new NavigationEntity();
        navigationEntity.setName(navigation.getName());

        return navigationEntity;
    };

    private NavigationMapper(){
        // hide constructor
    }
}
