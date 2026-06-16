package dao;

import entity.Todo;

import java.sql.SQLException;
import java.util.List;

public interface TodoDao {

    int save(Todo todo) throws SQLException;

    Todo findById(int id) throws SQLException;

    List<Todo> findAll() throws SQLException;

    int update(Todo todo) throws SQLException;

    int deleteById(int id) throws SQLException;
}