package com.momo.toys.be.validation;

import java.util.ArrayList;
import java.util.List;

import com.momo.toys.be.account.Role;
import org.springframework.web.multipart.MultipartFile;

public class ValidationData{

    private String email;

    private String password;

    private List<Role> roles = new ArrayList<>();

    private String navigationName;

    private String productName;

    private String productCode;

    private Integer amountProduct;

    private Integer quantityProduct;

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getProductCode(){
        return productCode;
    }

    public void setProductCode(String productCode){
        this.productCode = productCode;
    }

    public MultipartFile multipartFile;

    public Integer getAmountProduct() {
        return amountProduct;
    }

    public ValidationData setAmountProduct(Integer amountProduct) {
        this.amountProduct = amountProduct;
        return this;
    }

    public Integer getQuantityProduct() {
        return quantityProduct;
    }

    public ValidationData setQuantityProduct(Integer quantityProduct) {
        this.quantityProduct = quantityProduct;
        return this;
    }

    public String getNavigationName() {
        return navigationName;
    }

    public ValidationData setNavigationName(String navigationName) {
        this.navigationName = navigationName;
        return this;
    }

    public String getEmail(){
        return email;
    }

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
