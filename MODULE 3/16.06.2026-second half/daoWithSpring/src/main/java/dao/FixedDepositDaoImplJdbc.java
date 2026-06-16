package dao;

import Connection.DBmanager;
import entity.FixedDeposit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FixedDepositDaoImplJdbc implements FixedDepositDao {

    private final DBmanager dBmanager;
    public FixedDepositDaoImplJdbc(DBmanager dBmanager) {
        this.dBmanager = dBmanager;
    }
    public FixedDeposit mapToFixedDeposit(ResultSet rs) throws SQLException {

        return new FixedDeposit(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5));
    }

    @Override
    public void save(FixedDeposit fixedDeposit) throws SQLException {

        Connection con = dBmanager.getConnection();
        String sql = "insert into fixed_deposit values(?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, fixedDeposit.getFdId());
        ps.setString(2, fixedDeposit.getCustomerName());
        ps.setDouble(3, fixedDeposit.getDepositAmount());
        ps.setDouble(4, fixedDeposit.getInterestRate());
        ps.setInt(5, fixedDeposit.getTenureMonths());
        int row = ps.executeUpdate();
        con.close();
        System.out.println("Rows added: " + row);
    }

    @Override
    public FixedDeposit findById(int fdId) throws SQLException {

        Connection con = dBmanager.getConnection();
        String sql = "select * from fixed_deposit where fd_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, fdId);
        ResultSet rs = ps.executeQuery();
        FixedDeposit fd = null;
        if(rs.next()){
            fd = mapToFixedDeposit(rs);
        }
        con.close();
        return fd;
    }

    @Override
    public Collection<FixedDeposit> findAll() throws SQLException {

        List<FixedDeposit> fdList = new LinkedList<>();
        Connection con = dBmanager.getConnection();
        String sql = "select * from fixed_deposit";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            fdList.add(mapToFixedDeposit(rs));
        }
        con.close();
        return fdList;
    }

    @Override
    public void update(int fdId, FixedDeposit fixedDeposit) throws SQLException {

        Connection con = dBmanager.getConnection();
        String sql = "update fixed_deposit set customer_name=?,deposit_amount=?,interest_rate=?,tenure_months=? where fd_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, fixedDeposit.getCustomerName());
        ps.setDouble(2, fixedDeposit.getDepositAmount());
        ps.setDouble(3, fixedDeposit.getInterestRate());
        ps.setInt(4, fixedDeposit.getTenureMonths());
        ps.setInt(5, fdId);
        int row = ps.executeUpdate();
        con.close();
        System.out.println("Rows updated: " + row);
    }

    @Override
    public void deleteById(int fdId) throws SQLException {

        Connection con = dBmanager.getConnection();
        String sql = "delete from fixed_deposit where fd_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, fdId);
        int row = ps.executeUpdate();
        con.close();
        System.out.println("Rows deleted: " + row);
    }
}