package com.momo.toys.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.toys.be.entity.DocumentEntity;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long>{
}
