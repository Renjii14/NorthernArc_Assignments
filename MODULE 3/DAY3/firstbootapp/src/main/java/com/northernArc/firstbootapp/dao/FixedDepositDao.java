package com.northernArc.firstbootapp.dao;

import com.northernArc.firstbootapp.model.FixedDeposit;

import java.sql.SQLException;
import java.util.Collection;

public interface FixedDepositDao {

    void save(FixedDeposit fixedDeposit) throws SQLException;

    FixedDeposit findById(int fdId) throws SQLException;

    Collection<FixedDeposit> findAll() throws SQLException;

    void update(int fdId, FixedDeposit fixedDeposit) throws SQLException;

    void deleteById(int fdId) throws SQLException;
}
