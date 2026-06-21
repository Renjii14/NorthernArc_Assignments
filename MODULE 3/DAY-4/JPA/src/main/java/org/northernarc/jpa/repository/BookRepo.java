package org.northernarc.jpa.repository;

import org.northernarc.jpa.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BookRepo extends JpaRepository<Book,Integer>{

}
