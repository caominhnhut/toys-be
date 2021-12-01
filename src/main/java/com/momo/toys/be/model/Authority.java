package com.momo.toys.be.model;

import com.momo.toys.be.enumeration.AuthorityName;

public class Authority{

    private AuthorityName authorityName;

    public AuthorityName getAuthorityName(){
        return authorityName;
    }

    public void setAuthorityName(AuthorityName authorityName){
        this.authorityName = authorityName;
    }
}
