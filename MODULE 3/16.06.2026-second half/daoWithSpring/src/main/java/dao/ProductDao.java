package dao;

import entity.Product;

import java.sql.SQLException;
import java.util.Collection;

public interface ProductDao {

    void save(Product product) throws SQLException;
    Product findById(int id) throws SQLException;
    Collection<Product> findAll() throws SQLException;
    void update(int id,Product product) throws SQLException;
    void deleteById(int id) throws SQLException;

}
