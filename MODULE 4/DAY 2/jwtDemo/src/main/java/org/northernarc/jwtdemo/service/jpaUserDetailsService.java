package org.northernarc.jwtdemo.service;

import org.northernarc.jwtdemo.model.jpaUser;
import org.northernarc.jwtdemo.repo.jpaUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class jpaUserDetailsService implements UserDetailsService {
    @Autowired
    jpaUserRepo jpaUserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        jpaUser jpauser= jpaUserRepo.findByUsername(username);
        return User.builder().username(jpauser.getUsername())
                .password(jpauser.getPassword())
                .roles(jpauser.getRole())
                .build();
    }
}
