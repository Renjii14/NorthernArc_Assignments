package org.northernarc.productspringdemo.controller;

import jakarta.validation.Valid;
import org.northernarc.productspringdemo.DTO.CustomerRequestDTO;
import org.northernarc.productspringdemo.DTO.CustomerResponseDTO;
import org.northernarc.productspringdemo.DTO.CustomerUpdateDTO;
import org.northernarc.productspringdemo.DTO.OrderResponseDTO;
import org.northernarc.productspringdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> findAll() {

        return ResponseEntity.ok(
                customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(
            @Valid @RequestBody CustomerRequestDTO dto) {

        return ResponseEntity.status(201)
                .body(customerService.saveCustomer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerUpdateDTO dto) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getCustomerOrders(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                customerService.getCustomerOrders(id));
    }
}