package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.ProductRequestDTO;
import org.northernarc.productspringdemo.DTO.ProductResponseDTO;
import org.northernarc.productspringdemo.DTO.ProductUpdateDTO;
import org.northernarc.productspringdemo.exception.ProductNotFound;
import org.northernarc.productspringdemo.model.Product;
import org.northernarc.productspringdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Override
    public ProductResponseDTO saveProduct(ProductRequestDTO dto) {

        Product product = mapToEntity(dto);

        Product savedProduct = productRepo.save(product);

        return mapToResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {

        return productRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(Integer id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFound("Product not found"));

        return mapToResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id,
                                            ProductUpdateDTO dto) {

        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFound("Product not found"));

        if (dto.getName() != null)
            product.setName(dto.getName());

        if (dto.getBrand() != null)
            product.setBrand(dto.getBrand());

        if (dto.getCategory() != null)
            product.setCategory(dto.getCategory());

        if (dto.getCost() != null)
            product.setCost(dto.getCost());

        Product updatedProduct = productRepo.save(product);

        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFound("Product not found"));

        productRepo.delete(product);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {

        return productRepo.findProductByCategory(category)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsByBrand(String brand) {

        return productRepo.findProductByBrand(brand)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsByName(String name) {

        return productRepo.findProductByName(name)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ==========================
    // Mapper Methods
    // ==========================

    private Product mapToEntity(ProductRequestDTO dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setCategory(dto.getCategory());
        product.setCost(dto.getCost());

        return product;
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getCost()
        );
    }
}