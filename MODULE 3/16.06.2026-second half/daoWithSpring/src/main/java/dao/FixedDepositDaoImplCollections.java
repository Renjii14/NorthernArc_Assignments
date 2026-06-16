package dao;

import entity.FixedDeposit;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class FixedDepositDaoImplCollections implements FixedDepositDao {

    Map<Integer, FixedDeposit> map = new LinkedHashMap<>();

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
}