package com.momo.toys.be.entity;

import javax.persistence.*;

@Entity
@Table(name = "document")
public class DocumentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", unique = true)
    private String filename;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_uri")
    private String fileUri;

    @Column(name = "file_size")
    private Long fileSize;

    public ProductEntity getProduct(){
        return product;
    }

    public void setProduct(ProductEntity product){
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ReviewEntity getReview() {
        return review;
    }

    public void setReview(ReviewEntity review) {
        this.review = review;
    }
}
