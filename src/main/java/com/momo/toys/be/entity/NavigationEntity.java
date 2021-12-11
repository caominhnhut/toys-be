package com.momo.toys.be.entity;

import javax.persistence.*;

@Entity
@Table(name = "navigation")
@SequenceGenerator(name = "navigation_id_generator", sequenceName = "navigation_id_seq", allocationSize = 1)
public class NavigationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "navigation_id_generator")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

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

}
