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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "category")
@SequenceGenerator(name = "category_id_generator", sequenceName = "category_id_seq", allocationSize = 1)
public class CategoryEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_generator")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "navigation_id", nullable = false)
    private NavigationEntity navigation;

    @OneToMany(mappedBy = "categoryEntity")
    private Set<ProductEntity> productEntity;

    public Set<ProductEntity> getProductEntity(){
        return productEntity;
    }

    public void setProductEntity(Set<ProductEntity> productEntity){
        this.productEntity = productEntity;
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
