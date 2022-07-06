package com.momo.toys.be.repository.impl;

import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.repository.CustomUserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<UserEntity> findByStatus(String status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> users = cq.from(UserEntity.class);
        cq.select(users);
        cq.where(cb.equal(users.get("status"), EntityStatus.valueOf(status)));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public UserEntity findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> users = cq.from(UserEntity.class);
        cq.select(users);
        cq.where(cb.equal(users.get("email"), name));
        return em.createQuery(cq).getSingleResult();
    }


}
