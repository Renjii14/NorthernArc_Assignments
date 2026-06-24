package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.ProductRequestDTO;
import org.northernarc.productspringdemo.DTO.ProductResponseDTO;
import org.northernarc.productspringdemo.DTO.ProductUpdateDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ProductService {

    @PreAuthorize("hasRole('ADMIN')")
    ProductResponseDTO saveProduct(ProductRequestDTO productDTO);

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    List<ProductResponseDTO> getAllProducts();

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    ProductResponseDTO getProductById(Integer id);

    @PreAuthorize("hasRole('ADMIN')")
    ProductResponseDTO updateProduct(Integer id,
                                     ProductUpdateDTO productDTO);

    @PreAuthorize("hasRole('ADMIN')")
    void deleteProduct(Integer id);

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    List<ProductResponseDTO> getProductsByCategory(String category);

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    List<ProductResponseDTO> getProductsByBrand(String brand);

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    List<ProductResponseDTO> getProductsByName(String name);
}