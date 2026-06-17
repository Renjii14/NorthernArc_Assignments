package com.northernArc.firstbootapp.dao;


import com.northernArc.firstbootapp.model.Todo;

import java.sql.SQLException;
import java.util.Map;

public interface TodoDao {
    public void save(Todo todo);
    Todo findById(int id) throws SQLException;

    Map<Integer,Todo> findAll() throws SQLException;

    void deleteById(int id) throws SQLException;

    void update(
            int id,
            Todo todo)
            throws SQLException;
}
