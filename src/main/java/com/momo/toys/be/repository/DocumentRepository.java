package com.momo.toys.be.repository;

import com.momo.toys.be.entity.DocumentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {
}
