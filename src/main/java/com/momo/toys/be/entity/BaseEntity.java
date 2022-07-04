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
        createdDate= Calendar.getInstance().getTime();
        updatedDate=null;
        this.status = EntityStatus.ACTIVATED;
    }

    @PreUpdate
    public void onUpdate() {
        updatedDate= Calendar.getInstance().getTime();
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
