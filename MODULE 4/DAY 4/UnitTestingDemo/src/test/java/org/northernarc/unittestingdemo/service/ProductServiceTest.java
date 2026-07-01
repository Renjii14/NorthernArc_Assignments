package org.northernarc.unittestingdemo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.unittestingdemo.model.Product;
import org.northernarc.unittestingdemo.repository.ProductRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepo productRepo;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("Test addind a valid product")
    void testaddProduct() {
        Product product1 = new Product(1, "Laptop");
        Mockito.when(productRepo.existsById(1)).thenReturn(false);
        Mockito.when(productRepo.save(product1)).thenReturn(product1);
        Product savedProduct = productService.createProduct(product1);
        assertEquals(product1, savedProduct);
        Assertions.assertEquals(savedProduct.getName(),product1.getName());
        Assertions.assertEquals(savedProduct.getId(),product1.getId());
        Assertions.assertNotNull(savedProduct);
        Mockito.verify(productRepo).existsById(1);
        Mockito.verify(productRepo).save(product1);
    }

    @Test
    void testGetProductById() {
        Product product = new Product(1, "Laptop");
        Mockito.when(productRepo.findById(1)).thenReturn(Optional.of(product));
        Product result = productService.getProductById(1);
        assertEquals(product, result);
        Mockito.verify(productRepo).findById(1);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(
                new Product(1, "Laptop"),
                new Product(2, "Mobile")
        );
        Mockito.when(productRepo.findAll()).thenReturn(products);
        List<Product> result = productService.getAllProducts();
        assertEquals(2, result.size());
        assertEquals(products, result);
        Mockito.verify(productRepo).findAll();
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(10, "Laptop Updated");
        Mockito.when(productRepo.existsById(1)).thenReturn(true);
        Mockito.when(productRepo.save(product)).thenReturn(product);
        Product updated = productService.updateProduct(1, product);
        assertEquals(1, updated.getId());
        assertEquals("Laptop Updated", updated.getName());
        Mockito.verify(productRepo).existsById(1);
        Mockito.verify(productRepo).save(product);
    }

    @Test
    void testDeleteProduct() {
        Mockito.when(productRepo.existsById(1)).thenReturn(true);
        productService.deleteProduct(1);
        Mockito.verify(productRepo).existsById(1);
        Mockito.verify(productRepo).deleteById(1);
    }

    @Test
    void testCreateProductThrowsWhenAlreadyExists() {
        Product product = new Product(1, "Laptop");
        Mockito.when(productRepo.existsById(1)).thenReturn(true);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );
        assertTrue(exception.getMessage().contains("already exists"));
        Mockito.verify(productRepo).existsById(1);
    }

    @Test
    void testGetProductByIdThrowsWhenNotFound() {
        Mockito.when(productRepo.findById(1)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.getProductById(1)
        );
        assertTrue(exception.getMessage().contains("No product found"));
        Mockito.verify(productRepo).findById(1);
    }
}
