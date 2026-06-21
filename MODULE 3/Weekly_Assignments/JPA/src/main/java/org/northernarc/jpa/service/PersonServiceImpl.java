package org.northernarc.jpa.service;

import org.northernarc.jpa.model.Person;
import org.northernarc.jpa.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepo personRepo;

    @Override
    public Person save(Person person) {
        return personRepo.save(person);
    }

    @Override
    public Person findById(int id) {
        return personRepo.findById(id).get();
    }

    @Override
    public void deleteById(int id) {
        personRepo.deleteById(id);
    }

    @Override
    public Person update(int id, Person person) {

        Person person1 = personRepo.findById(id).get();

        person1.setName(person.getName());
        person1.setEmail(person.getEmail());
        person1.setAge(person.getAge());

        personRepo.save(person1);

        return person1;
    }

    @Override
    public Collection<Person> findAll() {
        return personRepo.findAll();
    }
}