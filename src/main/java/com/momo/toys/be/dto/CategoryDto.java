package com.momo.toys.be.dto;

public class CategoryDto {

    private Long id;

    private String name;

    private String status;

    private Long navigationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNavigationId() {
        return navigationId;
    }

    public void setNavigationId(Long navigationId) {
        this.navigationId = navigationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
