package org.northernarc.jpa.service;

import org.northernarc.jpa.model.Product;
import org.northernarc.jpa.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product findById(int id) {
        return productRepo.findById(id).get();
    }

    @Override
    public void deleteById(int id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product update(int id, Product product) {

        Product product1 = productRepo.findById(id).get();

        product1.setProduct_name(product.getProduct_name());
        product1.setPrice(product.getPrice());

        productRepo.save(product1);

        return product1;
    }

    @Override
    public Collection<Product> findAll() {
        return productRepo.findAll();
    }
}