package com.momo.toys.be.repository.impl;

import com.momo.toys.be.entity.OrderDetailEntity;
import com.momo.toys.be.entity.OrderEntity;
import com.momo.toys.be.repository.CustomOrderDetailRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.List;


@Service
public class CustomOrderDetailRepositoryImpl implements CustomOrderDetailRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<OrderDetailEntity> findOrderDetailByOrder(Long Id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderDetailEntity> cq = cb.createQuery(OrderDetailEntity.class);
        Root<OrderDetailEntity> orderDetailEntity = cq.from(OrderDetailEntity.class);
        cq.select(orderDetailEntity);
        cq.where(cb.equal(orderDetailEntity.get("primaryKey").get("orderEntity"), Id));
        return em.createQuery(cq).getResultList();

    }

}
