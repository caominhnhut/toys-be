package com.momo.toys.be.entity;

import com.momo.toys.be.enumeration.EntityStatus;

import javax.persistence.*;
import java.util.Calendar;

@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_date")
    protected Calendar createdDate;

    @Column(name = "updated_date")
    protected Calendar updatedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @PrePersist
    public void onCreate() {
        createdDate= Calendar.getInstance();
        updatedDate=null;
        this.status = EntityStatus.ACTIVE;
    }

    @PreUpdate
    public void onUpdate() {
        updatedDate= Calendar.getInstance();
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
