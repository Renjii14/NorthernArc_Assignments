package org.northernarc.unittestingdemo.service;

import org.northernarc.unittestingdemo.model.Product;
import org.northernarc.unittestingdemo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product createProduct(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("Product and product id cannot be null");
        }
        if (productRepo.existsById(product.getId())) {
            throw new IllegalArgumentException("Product already exists with id: " + product.getId());
        }
        return productRepo.save(product);
    }

    public Product getProductById(Integer id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No product found with id: " + id));
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product updateProduct(Integer id, Product product) {
        if (id == null || product == null) {
            throw new IllegalArgumentException("Product id and product cannot be null");
        }
        if (!productRepo.existsById(id)) {
            throw new IllegalArgumentException("No product found with id: " + id);
        }
        product.setId(id);
        return productRepo.save(product);
    }

    public void deleteProduct(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        if (!productRepo.existsById(id)) {
            throw new IllegalArgumentException("No product found with id: " + id);
        }
        productRepo.deleteById(id);
    }
}
