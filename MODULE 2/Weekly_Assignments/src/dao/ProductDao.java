package dao;

import entity.Product;

import java.sql.SQLException;
import java.util.Collection;

public interface ProductDao {

    int save(Product product) throws SQLException;

    Collection<Product> findAll() throws SQLException ;

    Collection<Product> findByCategory(String category) throws SQLException ;

    Collection<Product> findByPriceRange(
            double min,
            double max) throws SQLException ;

    Collection<Product> findByCategoryAndBrand(
            String category,
            String brand) throws SQLException ;

    Collection<Product> sortByPrice() throws SQLException ;

    Collection<Product> sortByPriceAndRating() throws SQLException ;

    double averagePrice() throws SQLException ;

    void groupByCategory() throws SQLException ;

    Collection<Product> topNExpensiveProducts(int n) throws SQLException ;
}
