package dao;

import entity.Product;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoImplCollections implements ProductDao{
    Map<Integer,Product> map=new LinkedHashMap<>();
    @Override
    public void save(Product product) throws SQLException {
            map.put(product.getProduct_id(), product);
    }

    @Override
    public Product findById(int id) throws SQLException {
        return map.get(id);
    }

    @Override
    public Collection<Product> findAll() throws SQLException {
        return map.values();
    }

    @Override
    public void update(int id,Product product) throws SQLException {
          map.put(id,product);
    }

    @Override
    public void deleteById(int id) throws SQLException {
           map.remove(id);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
