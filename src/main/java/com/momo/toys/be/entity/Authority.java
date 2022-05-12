package com.momo.toys.be.entity;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import com.momo.toys.be.enumeration.AuthorityName;

import java.util.List;

@Entity
@Table(name = "authority")

public class Authority extends BaseEntity implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private AuthorityName name;

    @ManyToMany(targetEntity = UserEntity.class, mappedBy = "authorities", cascade = {CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    private List<UserEntity> users;

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public void setName(AuthorityName name){
        this.name = name;
    }

    @Override
    public String getAuthority(){
        return name.name();
    }

    public Long getId(){
        return id;
    }

    public AuthorityName getName(){
        return name;
    }

    public void setId(Long id){
        this.id = id;
    }
}
