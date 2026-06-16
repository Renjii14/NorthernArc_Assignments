package dao;

import entity.Deposit;

import java.sql.SQLException;
import java.util.List;

public interface DepositDao {

    void create(Deposit deposit) throws SQLException;

    List<Deposit> findAll() throws SQLException;

    Deposit findById(int id) throws SQLException;

    void update(Deposit deposit) throws SQLException;

    void delete(int id) throws SQLException;
}