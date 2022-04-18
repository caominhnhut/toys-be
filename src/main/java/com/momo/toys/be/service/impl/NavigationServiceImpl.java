package com.momo.toys.be.service.impl;

import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.NavigationMapper;
import com.momo.toys.be.model.Navigation;
import com.momo.toys.be.repository.NavigationRepository;
import com.momo.toys.be.service.NavigationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class NavigationServiceImpl implements NavigationService {

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private NavigationRepository navigationRepository;

    @Override
    public Long create(Navigation navigation) {
        NavigationEntity navigationEntity = NavigationMapper.mapToEntity.apply(navigation);

        NavigationEntity createdNavigation = navigationRepository.save(navigationEntity);

        return createdNavigation.getId();
    }

    @Override
    public NavigationEntity findById(Long id) throws NotFoundException {
        Optional<NavigationEntity> optionalNavigationEntity = navigationRepository.findById(id);
        return optionalNavigationEntity.orElseThrow(()-> new NotFoundException("Not found Navigation with id = ".concat(id.toString())));
    }

}
