package org.northernarc.securitydemo.repository;

import org.northernarc.securitydemo.model.jpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface jpaUserRepo extends JpaRepository<jpaUser, Integer> {
    jpaUser findByUsername(String username);
}