package com.momo.toys.be.service;

import com.momo.toys.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.UserEntity;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        List<UserEntity> userEntities = userRepository.findByEmail(username);
        if(userEntities.isEmpty()){
            throw new UsernameNotFoundException(String.format("The user with username [%s] is not found",username));
        }
        return userEntities.get(0);
    }
}
