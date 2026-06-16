package dao;

import entity.Deposit;
import Connection.DBmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepositDaoImpl implements DepositDao {

    private Connection connection;

    public DepositDaoImpl() throws SQLException {
        connection = DBmanager.getConnection();
    }

    @Override
    public void create(Deposit deposit)
            throws SQLException {

        String sql =
                "INSERT INTO deposit(customer_name,deposit_amount,tenure_months,interest_rate) VALUES(?,?,?,?)";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setString(1, deposit.getCustomerName());
        ps.setDouble(2, deposit.getDepositAmount());
        ps.setInt(3, deposit.getTenureMonths());
        ps.setDouble(4, deposit.getInterestRate());

        ps.executeUpdate();

        System.out.println("Deposit Added");
    }

    @Override
    public List<Deposit> findAll()
            throws SQLException {

        List<Deposit> deposits =
                new ArrayList<>();

        String sql =
                "SELECT * FROM deposit";

        Statement st =
                connection.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

        while(rs.next()) {

            Deposit d = new Deposit(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getDouble("deposit_amount"),
                    rs.getInt("tenure_months"),
                    rs.getDouble("interest_rate")
            );

            deposits.add(d);
        }

        return deposits;
    }

    @Override
    public Deposit findById(int id)
            throws SQLException {

        String sql =
                "SELECT * FROM deposit WHERE id=?";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setInt(1,id);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return new Deposit(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getDouble("deposit_amount"),
                    rs.getInt("tenure_months"),
                    rs.getDouble("interest_rate")
            );
        }

        return null;
    }

    @Override
    public void update(Deposit deposit)
            throws SQLException {

        String sql =
                "UPDATE deposit SET customer_name=?,deposit_amount=?,tenure_months=?,interest_rate=? WHERE id=?";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setString(1,
                deposit.getCustomerName());

        ps.setDouble(2,
                deposit.getDepositAmount());

        ps.setInt(3,
                deposit.getTenureMonths());

        ps.setDouble(4,
                deposit.getInterestRate());

        ps.setInt(5,
                deposit.getId());

        ps.executeUpdate();

        System.out.println("Deposit Updated");
    }

    @Override
    public void delete(int id)
            throws SQLException {

        String sql =
                "DELETE FROM deposit WHERE id=?";

        PreparedStatement ps =
                connection.prepareStatement(sql);

        ps.setInt(1,id);

        ps.executeUpdate();

        System.out.println("Deposit Deleted");
    }
}