package org.northernarc.productspringdemo.controller;

import jakarta.validation.Valid;
import org.northernarc.productspringdemo.DTO.OrderRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderResponseDTO;
import org.northernarc.productspringdemo.DTO.OrderUpdateDTO;
import org.northernarc.productspringdemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll() {

        return ResponseEntity.ok(
                orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> save(
            @Valid @RequestBody OrderRequestDTO orderRequestDTO) {

        return ResponseEntity.status(201)
                .body(orderService.placeOrder(orderRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody OrderUpdateDTO dto) {

        return ResponseEntity.ok(
                orderService.updateOrder(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id) {

        orderService.cancelOrder(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(id));
    }
}