package org.northernarc.productspringdemo.controller;

import jakarta.validation.Valid;
import org.northernarc.productspringdemo.DTO.OrderItemRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderItemResponseDTO;
import org.northernarc.productspringdemo.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/orderitem")
public class OrderitemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemResponseDTO>> findAll() {

        return ResponseEntity.ok(
                orderItemService.getAllOrderItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDTO> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderItemService.getOrderItemById(id));
    }

    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> save(
            @Valid @RequestBody OrderItemRequestDTO dto) {

        return ResponseEntity.status(201)
                .body(orderItemService.saveOrderItem(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id) {

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderItemResponseDTO>> getItemsByOrderId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderItemService.getItemsByOrder(id));
    }
}