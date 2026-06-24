package org.northernarc.securitydemo.service;

import jakarta.annotation.PostConstruct;
import org.northernarc.securitydemo.model.jpaUser;
import org.northernarc.securitydemo.repository.jpaUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JPAUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    jpaUserRepo jpaUserRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws NullPointerException {
        jpaUser jpaUser=jpaUserRepo.findByUsername(username);
        return
                User.builder().username(jpaUser.getUsername()).password(jpaUser.getPassword()).roles(jpaUser.getRole()).build();

    }
//RUN THIS ONLY ONCE
    @PostConstruct
    public void init(){
        jpaUser jpaUser=new jpaUser();
        jpaUser.setUsername("user");
        jpaUser.setPassword(passwordEncoder.encode("123"));
        jpaUser.setRole("USER");

        jpaUser jpaUser1=new jpaUser();
        jpaUser1.setUsername("admin");
        jpaUser1.setPassword(passwordEncoder.encode("123"));
        jpaUser1.setRole("ADMIN");

        jpaUserRepo.save(jpaUser);
        jpaUserRepo.save(jpaUser1);

    }
}

