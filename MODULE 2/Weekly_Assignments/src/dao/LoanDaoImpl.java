package dao;

import entity.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDaoImpl implements LoanDao {

    @Override
    public int save(Loan loan) throws SQLException {

        String sql =
                "insert into loan values(?,?,?)";

        Connection con =
                DBmanager.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, loan.getLoanId());
        ps.setString(2, loan.getCustomerName());
        ps.setDouble(3, loan.getAmount());

        return ps.executeUpdate();
    }

    @Override
    public Loan findById(int id)
            throws SQLException {

        String sql =
                "select * from loan where loan_id=?";

        Connection con =
                DBmanager.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            return new Loan(
                    rs.getInt("loan_id"),
                    rs.getString("customer_name"),
                    rs.getDouble("amount")
            );
        }

        return null;
    }

    @Override
    public List<Loan> findAll()
            throws SQLException {

        List<Loan> loans =
                new ArrayList<>();

        String sql =
                "select * from loan";

        Connection con =
                DBmanager.getConnection();

        Statement st =
                con.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

        while(rs.next()) {

            loans.add(
                    new Loan(
                            rs.getInt("loan_id"),
                            rs.getString("customer_name"),
                            rs.getDouble("amount")
                    )
            );
        }

        return loans;
    }

    @Override
    public int update(Loan loan)
            throws SQLException {

        String sql =
                "update loan set customer_name=?, amount=? where loan_id=?";

        Connection con =
                DBmanager.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1,
                loan.getCustomerName());

        ps.setDouble(2,
                loan.getAmount());

        ps.setInt(3,
                loan.getLoanId());

        return ps.executeUpdate();
    }

    @Override
    public int deleteById(int id)
            throws SQLException {

        String sql =
                "delete from loan where loan_id=?";

        Connection con =
                DBmanager.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, id);

        return ps.executeUpdate();
    }
}