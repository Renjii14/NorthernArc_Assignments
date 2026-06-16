package dao;

import entity.Loan;

import java.sql.SQLException;
import java.util.List;

public interface LoanDao {

    int save(Loan loan) throws SQLException;

    Loan findById(int id) throws SQLException;

    List<Loan> findAll() throws SQLException;

    int update(Loan loan) throws SQLException;

    int deleteById(int id) throws SQLException;
}