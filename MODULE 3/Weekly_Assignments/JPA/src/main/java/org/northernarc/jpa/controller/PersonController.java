package org.northernarc.jpa.controller;

import org.northernarc.jpa.model.Person;
import org.northernarc.jpa.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public Collection<Person> listAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                personService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Person> save(
            @RequestBody Person person) {

        return ResponseEntity.status(201)
                .body(personService.save(person));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(
            @PathVariable int id,
            @RequestBody Person person) {

        return ResponseEntity.ok(
                personService.update(id, person)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable int id) {

        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}