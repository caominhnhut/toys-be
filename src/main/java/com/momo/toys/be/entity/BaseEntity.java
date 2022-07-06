package com.momo.toys.be.entity;

import com.momo.toys.be.enumeration.EntityStatus;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@MappedSuperclass
public class BaseEntity {

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @PrePersist
    public void onCreate() {
        createdDate = new Date();
        updatedDate = null;
        this.status = EntityStatus.ACTIVE;
    }

    @PreUpdate
    public void onUpdate() {
        updatedDate = new Date();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

}
