package com.northernArc.firstbootapp.dao;

import com.northernArc.firstbootapp.model.FixedDeposit;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
@Service
public class FixedDepositDaoImpl implements FixedDepositDao {

    Map<Integer, FixedDeposit> map;

    @Override
    public void save(FixedDeposit fixedDeposit) throws SQLException {
        map.put(fixedDeposit.getFdId(), fixedDeposit);
    }

    @Override
    public FixedDeposit findById(int fdId) throws SQLException {
        return map.get(fdId);
    }

    @Override
    public Collection<FixedDeposit> findAll() throws SQLException {
        return map.values();
    }

    @Override
    public void update(int fdId, FixedDeposit fixedDeposit) throws SQLException {
        map.put(fdId, fixedDeposit);
    }

    @Override
    public void deleteById(int fdId) throws SQLException {
        map.remove(fdId);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @PostConstruct
    public void init(){
        map=new HashMap<>();
        map.put(101,new FixedDeposit(101, "Rahul", 100000, 7.5, 12));
        map.put(102, new FixedDeposit(102, "Priya", 200000, 8.0, 24));
        map.put(103, new FixedDeposit(103, "Arun", 150000, 7.8, 18));
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Destroying...");
        map.clear();
    }
}
