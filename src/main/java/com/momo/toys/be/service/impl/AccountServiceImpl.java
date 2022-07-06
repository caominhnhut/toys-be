package com.momo.toys.be.service.impl;

import com.momo.toys.be.dto.AccountDto;
import com.momo.toys.be.entity.Authority;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.AuthorityName;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.AccountMapper;
import com.momo.toys.be.model.Account;
import com.momo.toys.be.repository.AuthorityRepository;
import com.momo.toys.be.repository.CustomUserRepository;
import com.momo.toys.be.repository.UserRepository;
import com.momo.toys.be.service.AccountService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Override
    public Long create(Account account) {

        UserEntity userEntity = AccountMapper.mapToEntity.apply(account);

        List<Authority> authorities = findAuthoritiesByName.apply(userEntity.getRoles());
        userEntity.setAuthorities(authorities);

        String encodedPassword = commonUtility.passwordEncoder().encode(account.getPassword());
        userEntity.setPassword(encodedPassword);

        UserEntity createdUser = userRepository.save(userEntity);

        return createdUser.getId();
    }

    @Override
    public boolean update(Account account) throws NotFoundException {
        List<Authority> authorities = findAuthoritiesByName.apply(AccountMapper.mapToAuthorityEntity.apply(account.getAuthorities()));
        Optional<UserEntity> optionalUserEntity = userRepository.findById(account.getId());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            userEntity.setAuthorities(authorities);
            try {
                userRepository.save(userEntity);
                return true;
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        throw new NotFoundException(String.format("Account with id [%s] not found", account.getId()));
    }

    @Override
    public boolean updateStatus(AccountDto accountDto) throws NotFoundException {

        Optional<UserEntity> userEntityOptional = userRepository.findById(accountDto.getId());
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            userEntity.setStatus(EntityStatus.valueOf(accountDto.getStatus()));
            userRepository.save(userEntity);
            return true;
        }
        throw new NotFoundException(String.format("Account with id [%s] not found", accountDto.getId()));

    }

    @Override
    public boolean delete(Long id) throws NotFoundException {

        userRepository.deleteById(id);
        return true;

    }


    @Override
    public Authentication getAuthorizedAccount() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public List<UserEntity> findAll() throws NotFoundException {

        List<UserEntity> userEntities = userRepository.findAll();
        if (userEntities.isEmpty()) {
            throw new NotFoundException("Users are not found");
        }

        return userEntities;
    }

    @Override
    public List<UserEntity> findStatus(String status) {
        List<UserEntity> userEntities = customUserRepository.findByStatus(status);
        return userEntities;
    }


    private UnaryOperator<List<Authority>> findAuthoritiesByName = authorities -> {

        List<Authority> result = authorities.stream().map(authority -> {
            List<Authority> subAuthority = authorityRepository.findByName(authority.getName());
            return subAuthority.get(0);
        }).collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException(String.format("The role [%s] not found", AuthorityName.ROLE_CUSTOMER.name()));
        }

        return result;
    };

    @Override
    public UserEntity findUserByName(String name){
        return customUserRepository.findByName(name);
    }

}
