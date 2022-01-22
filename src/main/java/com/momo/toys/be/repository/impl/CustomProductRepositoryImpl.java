package com.momo.toys.be.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.repository.CustomProductRepository;

@Service
public class CustomProductRepositoryImpl implements CustomProductRepository{

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ProductEntity> findByCriteria(String criteria){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);

        Root<ProductEntity> root = cq.from(ProductEntity.class);
        criteria = wildCard.apply(criteria);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.like(root.get("code"), criteria));
        predicates.add(cb.like(root.get("description"), criteria));
        predicates.add(cb.like(root.get("name"), criteria));

        cq.where(cb.or(predicates.toArray(new Predicate[0])));

        return em.createQuery(cq).getResultList();

    }

    private UnaryOperator<String> wildCard = criteria -> "%" + criteria + "%";
}
