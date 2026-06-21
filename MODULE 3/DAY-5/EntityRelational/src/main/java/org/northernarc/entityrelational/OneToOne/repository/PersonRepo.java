package org.northernarc.entityrelational.OneToOne.repository;

import org.northernarc.entityrelational.OneToOne.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person,Integer> {
}
