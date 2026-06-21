package org.northernarc.jpa.service;


import org.northernarc.jpa.model.Todo;

import java.util.Collection;
import java.util.Map;

public interface TodoService {

    Todo save(Todo todo);

    Todo findById(int id);

    Collection<Todo> findAll();

    void deleteById(int id);

    Todo update(
            int id,
            Todo todo);
}