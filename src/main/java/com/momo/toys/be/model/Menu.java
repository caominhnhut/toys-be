package com.momo.toys.be.model;

public class Menu{

    private String id;

    private String name;

    private String alias;

    public Menu(String id, String name, String alias){
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getAlias(){
        return alias;
    }

    public void setAlias(String alias){
        this.alias = alias;
    }
}
