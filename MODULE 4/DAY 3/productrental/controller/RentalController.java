package org.northernarc.productrental.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.northernarc.productrental.dto.CustomerRentalSummaryDTO;
import org.northernarc.productrental.exception.CustomerNotFoundException;
import org.northernarc.productrental.exception.ProductNotFoundException;
import org.northernarc.productrental.model.Customer;
import org.northernarc.productrental.model.Product;
import org.northernarc.productrental.model.RentalRecord;
import org.northernarc.productrental.repository.CustomerRepository;
import org.northernarc.productrental.repository.ProductRepository;
import org.northernarc.productrental.service.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Rental Management", description = "Customer, product and rental APIs")
@SecurityRequirement(name = "bearerAuth")
public class RentalController {

    private final RentalService rentalService;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a customer")
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @Operation(summary = "Get customer by id")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @GetMapping("/customers/{id}/summary")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @Operation(summary = "Get customer rental summary")
    public CustomerRentalSummaryDTO getCustomerSummary(@PathVariable Long id) {
        return customerRepository.findCustomerRentalSummary(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a product")
    public Product createProduct(@Valid @RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @Operation(summary = "Get products with pagination and sorting")
    public Page<Product> getProducts(Pageable pageable) {
        Pageable sorted = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "rentPerDay")
        );
        return productRepository.findAll(sorted);
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @Operation(summary = "Get product by id")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product by id (ADMIN)")
    public void deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    @PutMapping("/products/{id}/rent")
    @Transactional
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update product rent amount (MANAGER)")
    public Product updateRent(@PathVariable Long id, @RequestParam double amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setRentPerDay(product.getRentPerDay() + amount);
        return productRepository.save(product);
    }

    @GetMapping("/rentals")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @Operation(summary = "Get all rental records")
    public List<RentalRecord> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/rentals/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
    @Operation(summary = "Get rental record by id")
    public RentalRecord getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }
}
