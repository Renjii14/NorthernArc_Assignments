package org.northernarc.jpa.repository;

import org.northernarc.jpa.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepo extends JpaRepository<Todo,Integer> {
}
