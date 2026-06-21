package org.northernarc.jpa.controller;

import org.northernarc.jpa.model.Todo;
import org.northernarc.jpa.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<Map<Integer, Todo>> find() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Todo> findbyid(@PathVariable int id)  {
        return ResponseEntity.ok(todoService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id){
        todoService.deleteById(id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Todo> save(@PathVariable int id,@RequestBody Todo todo) throws SQLException {
       return ResponseEntity.status(201).body(todoService.save(todo));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable int id,@RequestBody Todo todo){
        return ResponseEntity.ok(todoService.update(id,todo));
    }
}
