package org.northernarc.jpa.service;

import org.northernarc.jpa.model.Todo;
import org.northernarc.jpa.repository.TodoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepo todoRepo;


    @Override
    public Todo save(Todo todo) {
        return todoRepo.save(todo);
    }

    @Override
    public Todo findById(int id) {
        return todoRepo.findById(id).get();
    }

    @Override
    public Collection<Todo> findAll()  {
        return todoRepo.findAll();
    }

    @Override
    public void deleteById(int id) {
      todoRepo.deleteById(id);
    }

    @Override
    public Todo update(int id, Todo todo) {
        Todo todo1=todoRepo.getById(id);
        todo1.setTodoId(todo.getTodoId());
        todo1.setTask(todo.getTask());
        todo1.setStatus(todo.getStatus());
        todoRepo.save(todo1);
        return todo1;
    }
}
