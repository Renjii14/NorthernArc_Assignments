package dao;

import entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    int save(Product product) throws SQLException;

    Product findById(int id) throws SQLException;

    List<Product> findAll() throws SQLException;

    int update(Product product) throws SQLException;

    int deleteById(int id) throws SQLException;
}