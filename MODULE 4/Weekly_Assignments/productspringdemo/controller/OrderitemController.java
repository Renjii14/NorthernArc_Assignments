package org.northernarc.productspringdemo.controller;

import jakarta.validation.Valid;
import org.northernarc.productspringdemo.DTO.OrderItemRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderItemResponseDTO;
import org.northernarc.productspringdemo.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/orderitem")
public class OrderitemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OrderItemResponseDTO> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderItemService.getOrderItemById(id));
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<OrderItemResponseDTO>> getItemsByOrderId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderItemService.getItemsByOrder(id));
    }
}