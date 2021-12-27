package com.momo.toys.be.factory.seed;

import java.util.Calendar;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import com.momo.toys.be.entity.mongo.MenuCollection;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.repository.MenuRepository;

@Service
public class MenuSeedService implements SeedDataService{

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${mongodb.menu.json.filename}")
    private String filename;

    @Override
    public void seed(){
        List<MenuCollection> menus = commonUtility.loadDataList(MenuCollection.class, filename);

        for(MenuCollection menu : menus){
            List<CategoryCollection> insertedCategories = saveCategories.apply(menu.getCategories());
            menu.getCategories().clear();
            menu.setCreatedDate(Calendar.getInstance().getTime());
            menu.setStatus(EntityStatus.ACTIVE);
            menu.getCategories().addAll(insertedCategories);

            menuRepository.save(menu);
        }
    }

    private UnaryOperator<List<CategoryCollection>> saveCategories = categories -> categories.stream().map(category -> {
        category.setCreatedDate(Calendar.getInstance().getTime());
        category.setStatus(EntityStatus.ACTIVE);
        return categoryRepository.save(category);
    }).collect(Collectors.toList());
}
