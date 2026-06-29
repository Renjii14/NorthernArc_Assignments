package org.northernarc.productspringdemo.repository;

import org.northernarc.productspringdemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory(String category);
    List<Product> findByName(String name);
    List<Product> findByBrand(String brand);

    List<Product> findProductByName(String name);

    List<Product> findProductByBrand(String brand);

    List<Product> findProductByCategory(String category);
}
