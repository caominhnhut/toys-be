package com.momo.toys.be.model;

public class Category{

    private Long id;

    private String name;

    private Long navigationId;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Long getNavigationId(){
        return navigationId;
    }

    public void setNavigationId(Long navigationId){
        this.navigationId = navigationId;
    }
}
