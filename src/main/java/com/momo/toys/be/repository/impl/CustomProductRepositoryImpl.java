package com.momo.toys.be.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.script.ScriptEngine;

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

        Predicate codePredicate = cb.like(root.get("code"), criteria);
        Predicate descriptionPredicate = cb.like(root.get("description"), criteria);
        Predicate namePredicate = cb.like(root.get("name"), criteria);

        Predicate[] predicates = {codePredicate, descriptionPredicate, namePredicate};

        cq.where(cb.or(predicates));

        return em.createQuery(cq).getResultList();

    }

    @Override
    public List<ProductEntity> findByDates(Date fromDate, Date toDate){

        String query = "select p from ProductEntity p where p.createdDate between :fromDate and :toDate";
        Query typedQuery = em.createQuery(query);
        typedQuery.setParameter("fromDate", fromDate, TemporalType.TIMESTAMP);
        typedQuery.setParameter("toDate", toDate, TemporalType.TIMESTAMP);

        return typedQuery.getResultList();
    }

    private UnaryOperator<String> wildCard = criteria -> "%" + criteria + "%";
}
