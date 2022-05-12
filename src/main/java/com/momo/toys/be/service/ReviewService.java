package com.momo.toys.be.service;

import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Review;
import javassist.NotFoundException;

public interface ReviewService {

    Long create(Review review) throws NotFoundException, InterruptedException;

    Long update(Review review) throws NotFoundException, FileStorageException;

    Boolean delete(Long reviewId, boolean isSoftDelete) throws NotFoundException;

}
