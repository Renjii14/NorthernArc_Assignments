package dao;

import entity.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersonDao {

    void create(Person person) throws SQLException;

    List<Person> findAll() throws SQLException;

    Person findById(int id) throws SQLException;

    void update(Person person) throws SQLException;

    void delete(int id) throws SQLException;
}