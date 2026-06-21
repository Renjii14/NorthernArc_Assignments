package org.northernarc.jpa.service;

import org.northernarc.jpa.model.Product;

import java.util.Collection;

public interface ProductService {

    Product save(Product product);

    Product findById(int id);

    Collection<Product> findAll();

    void deleteById(int id);

    Product update(
            int id,
            Product product);
}