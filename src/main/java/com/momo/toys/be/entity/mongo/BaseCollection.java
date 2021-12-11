package com.momo.toys.be.entity.mongo;

import com.momo.toys.be.enumeration.EntityStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Calendar;

@Document
public abstract class BaseCollection {

    @Id
    private String id;

    @Field(value = "status")
    private EntityStatus status;

    @Field(value = "created_date")
    private Calendar createdDate;

    @Field(value = "updated_date")
    private Calendar updatedDate;

    @PrePersist
    public void onCreate() {
        createdDate = Calendar.getInstance();
        updatedDate = null;
        status = EntityStatus.ACTIVE;
    }

    @PreUpdate
    public void onUpdate() {
        updatedDate = Calendar.getInstance();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public Calendar getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }
}
