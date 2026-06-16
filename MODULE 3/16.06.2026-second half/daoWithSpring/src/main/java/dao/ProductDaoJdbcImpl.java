package dao;

import Connection.DBmanager;
import entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ProductDaoJdbcImpl implements ProductDao{
    private final DBmanager dBmanager;

    public ProductDaoJdbcImpl(DBmanager dBmanager) {
        this.dBmanager = dBmanager;
    }

    public Product MaptoProduct(ResultSet rs) throws SQLException {
        return new Product( rs.getInt(1),rs.getString(2),rs.getDouble(3) );
    }

    @Override
    public void save(Product product) throws SQLException {
        Connection con=dBmanager.getConnection();
        String sql="Insert into product values(?,?,?)";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,product.getProduct_id());
        ps.setString(2, product.getProduct_name());
        ps.setDouble(3,product.getPrice());
        int row=ps.executeUpdate();
        con.close();
        System.out.println("Rows added:"+row);

    }

    @Override
    public Product findById(int id) throws SQLException {
        Connection con=dBmanager.getConnection();
        String sql="select * from product where product_id=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,id);
        ResultSet rs=ps.executeQuery();

        while(rs.next()) {
            return MaptoProduct(rs);
        }
        con.close();
        return null;

    }

    @Override
    public Collection<Product> findAll() throws SQLException {
        List<Product> plist=new LinkedList<>();
        Connection con=dBmanager.getConnection();
        String sql="select * from product";
        PreparedStatement ps=con.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        while(rs.next()){
            plist.add(MaptoProduct(rs));
        }
        con.close();
        return plist;

    }

    @Override
    public void update(int id,Product product) throws SQLException {
        Connection con=dBmanager.getConnection();
        String sql="update product set product_name=?,price=? where product_id=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, product.getProduct_name());
        ps.setDouble(2,product.getPrice());
        ps.setInt(3,product.getProduct_id());
        int row=ps.executeUpdate();
        con.close();
        System.out.println("Rows updated:"+row);
    }

    @Override
    public void deleteById(int id) throws SQLException {
        Connection con=dBmanager.getConnection();
        String sql="delete from product where product_id=?";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1,id);
        int row=ps.executeUpdate();
        con.close();
        System.out.println("Rows deleted:"+row);
    }
}
