package com.momo.toys.be.entity;

import javax.persistence.*;

@Entity
@Table(name = "document")
@SequenceGenerator(name = "document_id_generator", sequenceName = "document_id_seq", allocationSize = 1)
public class DocumentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_generator")
    private Long id;

    @Column(name = "file_name", unique = true)
    private String filename;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_uir")
    private String fileUri;

    @Column(name = "file_size")
    private Long fileSize;

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
}
