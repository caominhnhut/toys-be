package com.momo.toys.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.toys.be.entity.Authority;
import com.momo.toys.be.enumeration.AuthorityName;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

    List<Authority> findByName(AuthorityName authorityName);
}
