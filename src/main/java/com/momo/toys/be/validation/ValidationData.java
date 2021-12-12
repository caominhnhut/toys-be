package com.momo.toys.be.validation;

import java.util.ArrayList;
import java.util.List;

import com.momo.toys.be.account.Role;
import org.springframework.web.multipart.MultipartFile;

public class ValidationData{

    private String email;

    private String password;

    private List<Role> roles = new ArrayList<>();

    public String getEmail(){
        return email;
    }

    public MultipartFile multipartFile;

    public ValidationData setEmail(String email){
        this.email = email;
        return this;
    }

    public String getPassword(){
        return password;
    }

    public ValidationData setPassword(String password){
        this.password = password;
        return this;
    }

    public List<Role> getRoles(){
        return roles;
    }

    public ValidationData setRoles(List<Role> roles){
        this.roles = roles;
        return this;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public ValidationData setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        return this;
    }
}
