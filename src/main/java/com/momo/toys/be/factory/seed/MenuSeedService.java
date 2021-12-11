package com.momo.toys.be.factory.seed;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import com.momo.toys.be.entity.mongo.MenuCollection;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void seed() {
        List<MenuCollection> navigators = commonUtility.loadDataList(MenuCollection.class, filename);

        for(MenuCollection navigator: navigators){

            List<CategoryCollection> insertedCategories = categoryRepository.saveAll(navigator.getCategories());
            navigator.getCategories().clear();
            navigator.getCategories().addAll(insertedCategories);

            menuRepository.save(navigator);
        }
    }
}
