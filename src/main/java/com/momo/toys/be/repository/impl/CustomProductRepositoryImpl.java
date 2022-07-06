package com.momo.toys.be.repository.impl;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.repository.CustomProductRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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


    @Override
    public ProductEntity findProductById(Long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cq = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> product = cq.from(ProductEntity.class);
        cq.select(product);
        cq.where(cb.equal(product.get("id"), id));
        return em.createQuery(cq).getSingleResult();
    }

    private UnaryOperator<String> wildCard = criteria -> "%" + criteria + "%";
}

