package org.northernarc.jwtdemo.repo;

import org.northernarc.jwtdemo.model.jpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface jpaUserRepo extends JpaRepository<jpaUser,Integer> {
    public jpaUser findByUsername(String username);
}
