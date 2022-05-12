package com.momo.toys.be.service.impl;

import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.entity.ReviewEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ReviewMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Review;
import com.momo.toys.be.repository.ProductRepository;
import com.momo.toys.be.repository.ReviewRepository;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.service.ReviewService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    public Long create(Review review) throws NotFoundException, InterruptedException {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(review.getProductId());
        if(!productEntityOptional.isPresent()){
            throw new NotFoundException(String.format("Product with id [%s] not found", review.getProductId()));
        }

        if(review.getImages() != null) {
            for (Document image : review.getImages()) {
                String extension = StringUtils.getFilenameExtension(image.getFilename());
                String uniqueName = commonUtility.uniqueFileName.apply(extension);
                if (image.getFilename().equals(review.getMainImage())) {
                    review.setMainImage(uniqueName);
                }
                image.setFilename(uniqueName);

                // sleep in 1 milliseconds to get risk of duplicating image name
                TimeUnit.MILLISECONDS.sleep(1);
            }
        }

        ReviewEntity reviewEntity = ReviewMapper.mapModelToEntity.apply(review);
        reviewEntity.setProduct(productEntityOptional.get());
        reviewEntity.setCreatedBy(accountService.getAuthorizedAccount().getName());
        reviewRepository.save(reviewEntity);
        if(review.getImages() != null) {
            for (Document image : review.getImages()) {
                documentService.uploadComment(image, reviewEntity);
            }
        }
        return reviewEntity.getId();
    }

    @Override
    public Long update(Review review) throws NotFoundException, FileStorageException {
        Optional<ReviewEntity> existingReviewEntityOptional = reviewRepository.findById(review.getId());

        if(!existingReviewEntityOptional.isPresent()){
            throw new NotFoundException(String.format("Comment with id [%s] not found", review.getId()));
        }

        ReviewEntity reviewEntity = existingReviewEntityOptional.get();
        convertFromModelToEntity.accept(review, reviewEntity);

        List<Document> images = review.getImages();
        if(images == null || images.isEmpty()){
            reviewRepository.save(reviewEntity);
            return reviewEntity.getId();
        }

        List<String> newUniqueNames = new ArrayList<>();
        for(Document image : images){
            String extension = StringUtils.getFilenameExtension(image.getFilename());
            String uniqueName = commonUtility.uniqueFileName.apply(extension);
            if(image.getFilename().equals(review.getMainImage())){
                reviewEntity.setMainImage(uniqueName);
            }
            image.setFilename(uniqueName);
            newUniqueNames.add(uniqueName);
            documentService.uploadComment(image, reviewEntity);
        }

        reviewRepository.save(reviewEntity);

        // Delete existing images
        Set<DocumentEntity> existingImages = reviewEntity.getImages();
        for(DocumentEntity imageEntity : existingImages){

            if(newUniqueNames.contains(imageEntity.getFilename())){
                continue;
            }

            Document image = DocumentMapper.mapEntityToDocument.apply(imageEntity);
            documentService.delete(image);
        }

        return reviewEntity.getId();
    }

    @Override
    public Boolean delete(Long reviewId, boolean isSoftDelete) throws NotFoundException {
        Optional<ReviewEntity> optionalReviewEntity = reviewRepository.findById(reviewId);

        if(!optionalReviewEntity.isPresent()){
            throw new NotFoundException(String.format("Not found", reviewId));
        }

        ReviewEntity review = optionalReviewEntity.get();

        if(isSoftDelete){
            review.setStatus(EntityStatus.DELETED);
            reviewRepository.save(review);
            return true;
        }

        for(DocumentEntity image : review.getImages()){

            try{
                Files.deleteIfExists(Paths.get(image.getFileUri()));
                documentService.delete(DocumentMapper.mapEntityToDocument.apply(image));
            }catch(IOException | FileStorageException e){
                LOGGER.error(e.getMessage());
            }
        }
        reviewRepository.delete(review);
        return true;
    }


    private BiConsumer<Review, ReviewEntity> convertFromModelToEntity = (review, reviewEntity) -> {
        reviewEntity.setComment(review.getComment());
        reviewEntity.setRate(review.getRate());
    };
}
