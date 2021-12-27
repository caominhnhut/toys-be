package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.entity.mongo.MenuCollection;
import com.momo.toys.be.model.Menu;

public class MenuMapper{

    private MenuMapper(){
        // hidden constructor
    }

    public static final Function<MenuCollection, Menu> mapFromCollection = menu -> new Menu(menu.getId(), menu.getName(), menu.getAlias());
}
