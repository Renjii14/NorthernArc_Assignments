package org.northernarc.jpa.controller;

import org.northernarc.jpa.model.Product;
import org.northernarc.jpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Collection<Product> listAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                productService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Product> save(
            @RequestBody Product product) {

        return ResponseEntity.status(201)
                .body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable int id,
            @RequestBody Product product) {

        return ResponseEntity.ok(
                productService.update(id, product)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable int id) {

        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}