package org.northernarc.jpa.service;

import org.northernarc.jpa.model.Person;

import java.util.Collection;

public interface PersonService {

    Person save(Person person);

    Person findById(int id);

    Collection<Person> findAll();

    void deleteById(int id);

    Person update(
            int id,
            Person person);
}