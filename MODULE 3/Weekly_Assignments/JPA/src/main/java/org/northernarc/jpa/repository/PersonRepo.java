package org.northernarc.jpa.repository;

import org.northernarc.jpa.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person,Integer> {
}
