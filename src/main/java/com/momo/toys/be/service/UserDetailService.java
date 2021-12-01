package com.momo.toys.be.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.UserEntity;

@Service
public class UserDetailService implements UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String username){
        try{
            return mockUser(username);
        }catch(Exception e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    private UserEntity mockUser(String userName){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userName);
        return userEntity;
    }
}
