package dao;

import entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl
        implements ProductDao {

    @Override
    public int save(Product product)
            throws SQLException {

        String sql =
                "insert into product values(?,?,?)";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1,
                product.getProductId());

        ps.setString(2,
                product.getProductName());

        ps.setDouble(3,
                product.getPrice());

        return ps.executeUpdate();
    }

    @Override
    public Product findById(int id)
            throws SQLException {

        String sql =
                "select * from product where product_id=?";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price")
            );
        }

        return null;
    }

    @Override
    public List<Product> findAll()
            throws SQLException {

        List<Product> products =
                new ArrayList<>();

        String sql =
                "select * from product";

        Connection con =
                DBConnection.getConnection();

        Statement st =
                con.createStatement();

        ResultSet rs =
                st.executeQuery(sql);

        while(rs.next()) {

            products.add(
                    new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getDouble("price")
                    )
            );
        }

        return products;
    }

    @Override
    public int update(Product product)
            throws SQLException {

        String sql =
                "update product set product_name=?, price=? where product_id=?";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1,
                product.getProductName());

        ps.setDouble(2,
                product.getPrice());

        ps.setInt(3,
                product.getProductId());

        return ps.executeUpdate();
    }

    @Override
    public int deleteById(int id)
            throws SQLException {

        String sql =
                "delete from product where product_id=?";

        Connection con =
                DBConnection.getConnection();

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setInt(1, id);

        return ps.executeUpdate();
    }
}