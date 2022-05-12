package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.entity.ReviewEntity;
import com.momo.toys.be.model.Review;

import java.util.function.Function;

public class ReviewMapper {

    public static final Function<Review, ReviewEntity> mapModelToEntity = review -> {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setComment(review.getComment());
        reviewEntity.setRate(review.getRate());
        reviewEntity.setCreatedBy(review.getCreatedBy());
        reviewEntity.setMainImage(review.getMainImage());
        return reviewEntity;
    };

    public static final Function<com.momo.toys.be.review.Review, Review> mapDtoToModel = review -> {
        Review reviewModel = new Review();
        reviewModel.setComment(review.getComment());
        reviewModel.setRate(review.getRate());
        reviewModel.setCreatedBy(review.getCreatedBy());
        reviewModel.setMainImage(review.getMainImage());
        return reviewModel;
    };

    public static final Function<ReviewEntity, Review> mapEntityToModel = reviewEntity -> {
        Review reviewModel = new Review();
        reviewModel.setComment(reviewEntity.getComment());
        reviewModel.setRate(reviewEntity.getRate());
        reviewModel.setCreatedBy(reviewEntity.getCreatedBy());
        reviewModel.setMainImage(reviewEntity.getMainImage());
        return reviewModel;
    };

    public static final Function<Review, com.momo.toys.be.review.Review> mapModelToDto = reviewModel -> {
        com.momo.toys.be.review.Review reviewDto = new com.momo.toys.be.review.Review();
        reviewDto.setComment(reviewModel.getComment());
        reviewDto.setRate(reviewModel.getRate());
        reviewDto.setCreatedBy(reviewModel.getCreatedBy());
        reviewDto.setMainImage(reviewModel.getMainImage());
        return reviewDto;
    };

    private ReviewMapper() {
        // hide constructor
    }
}
