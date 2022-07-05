package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.navigation.Navigation;

public class NavigationMapper {

    private NavigationMapper(){
        // hide constructor
    }

    public static final Function<Navigation, com.momo.toys.be.model.Navigation> mapToModel = dto -> {
        com.momo.toys.be.model.Navigation model = new com.momo.toys.be.model.Navigation();
        model.setName(dto.getName());
        return model;
    };

    public static final Function<com.momo.toys.be.model.Navigation, NavigationEntity> mapToEntity = model -> {
        NavigationEntity entity = new NavigationEntity();
        entity.setName(model.getName());
        return entity;
    };

    public static final Function<NavigationEntity, com.momo.toys.be.model.Navigation> mapFromEntity = entity -> {
        com.momo.toys.be.model.Navigation model = new com.momo.toys.be.model.Navigation();
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    };

    public static final Function<com.momo.toys.be.model.Navigation, Navigation> mapFromModel = model -> {
        Navigation dto = new Navigation();
        dto.setId(model.getId());
        dto.setName(model.getName());
        return dto;
    };
}
