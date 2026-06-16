package dao;

import entity.Todo;

import java.sql.SQLException;
import java.util.Map;

public interface TodoDao {

    void save(Todo todo) throws SQLException;

    Todo findById(int id) throws SQLException;

    Map<Integer,Todo> findAll() throws SQLException;

    void deleteById(int id) throws SQLException;

    void update(
            int id,
            Todo todo)
            throws SQLException;
}
