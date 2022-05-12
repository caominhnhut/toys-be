package com.momo.toys.be.controller;

import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ReviewMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.review.Review;
import com.momo.toys.be.service.ReviewService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping("/product/{product-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> create(@PathVariable("product-id") Long productId, @RequestPart("review") Review review, @RequestPart(value = "files", required = false) List<MultipartFile> files) throws NotFoundException, InterruptedException {

        com.momo.toys.be.model.Review reviewModel = ReviewMapper.mapDtoToModel.apply(review);

        if (files != null) {
            List<Document> images = files.stream().map(DocumentMapper.mapToDocument).collect(Collectors.toList());
            reviewModel.setImages(images);
        }
        reviewModel.setProductId(productId);

        Long reviewId = reviewService.create(reviewModel);
        return ResponseEntity.status(HttpStatus.CREATED).body("comment added" + reviewId);

    }


    @PutMapping("/product/{product-id}/{review-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Long> editReview(@PathVariable("product-id") Long productId, @PathVariable("review-id") Long reviewId, @RequestPart("review") Review review, @RequestPart(value = "files", required = true) List<MultipartFile> files) throws NotFoundException, FileStorageException {

        com.momo.toys.be.model.Review reviewModel = ReviewMapper.mapDtoToModel.apply(review);

        List<Document> images = files.stream().map(DocumentMapper.mapToDocument).collect(Collectors.toList());
        reviewModel.setImages(images);
        reviewModel.setImages(images);
        reviewModel.setProductId(productId);

        reviewService.update(reviewModel);
        return ResponseEntity.status(HttpStatus.OK).body(reviewId);

    }

    @DeleteMapping("/{review-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Boolean> deleteReview(@PathVariable("review-id") Long reviewId, @RequestParam(name = "is-soft-delete", required = false, defaultValue = "false") Boolean isSoftDelete) throws NotFoundException {

        reviewService.delete(reviewId, isSoftDelete);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

}
