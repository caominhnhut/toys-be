package com.momo.toys.be.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "navigation_id", nullable = false)
    private NavigationEntity navigation;

    @OneToMany(mappedBy = "categoryEntity")
    private Set<ProductEntity> productEntities;

    public Set<ProductEntity> getProductEntities(){
        return productEntities;
    }

    public void setProductEntities(Set<ProductEntity> productEntities){
        this.productEntities = productEntities;
    }

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

    public NavigationEntity getNavigation(){
        return navigation;
    }

    public void setNavigation(NavigationEntity navigation){
        this.navigation = navigation;
    }

}
