package com.momo.toys.be.repository.impl;

import com.momo.toys.be.entity.OrderEntity;
import com.momo.toys.be.repository.CustomOrderRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Service
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<OrderEntity> findOrderByUser(Long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);
        Root<OrderEntity> order = cq.from(OrderEntity.class);
        cq.select(order);
        cq.where(cb.equal(order.get("user").get("id"), userId));
        return em.createQuery(cq).getResultList();

    }

    //
//    @Override
//    public List<OrderEntity> findOrderByDate(final Date startDate, final Date endDate) {
//
//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<OrderEntity> criteria = builder.createQuery(OrderEntity.class);
//        Root<OrderEntity> order = criteria.from(OrderEntity.class);
//        criteria.select(order);
//        criteria.where(builder.between(order.get("createdDate"), startDate, endDate));
//        TypedQuery<OrderEntity> query = em.createQuery(criteria);
//        query.setHint(QueryHints.HINT_CACHEABLE, true);
//        query.setHint(QueryHints.HINT_CACHE_REGION, "query.OrderEntity");
//        return query.getResultList();
//    }
    @Override
    public List<OrderEntity> findOrderByDate(Date startDate, Date endDate) {

        String query = "select p from OrderEntity p where p.createdDate between :startDate and :endDate";
        Query typedQuery = em.createQuery(query);
        typedQuery.setParameter("startDate", startDate, TemporalType.TIMESTAMP);
        typedQuery.setParameter("endDate", endDate, TemporalType.TIMESTAMP);

        return typedQuery.getResultList();

    }
//    @Override
//    public List<OrderEntity> findOrderByDate(Date fromDate, Date toDate) {
//
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);
//            Root<OrderEntity> order = cq.from(OrderEntity.class);
//          //  String a = order.get("createdDate").as(String.class);
//
//          //  Predicate predicate = cb.like(, "%"+fromDate+"%");
//
//            cq.where(predicate).select(order);
//
//           return em.createQuery(cq).getResultList();

//        return null;
//    }

}

