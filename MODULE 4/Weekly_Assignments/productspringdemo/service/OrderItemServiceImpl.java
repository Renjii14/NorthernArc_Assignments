package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.OrderItemRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderItemResponseDTO;
import org.northernarc.productspringdemo.exception.OrderNotFound;
import org.northernarc.productspringdemo.exception.ProductNotFound;
import org.northernarc.productspringdemo.model.Order;
import org.northernarc.productspringdemo.model.OrderItem;
import org.northernarc.productspringdemo.model.Product;
import org.northernarc.productspringdemo.repository.OrderItemRepository;
import org.northernarc.productspringdemo.repository.OrderRepository;
import org.northernarc.productspringdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Override
    public List<OrderItemResponseDTO> getAllOrderItems() {

        return orderItemRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public OrderItemResponseDTO getOrderItemById(Integer id) {

        OrderItem orderItem = orderItemRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order Item not found"));

        return mapToResponseDTO(orderItem);
    }


    @Override
    public List<OrderItemResponseDTO> getItemsByOrder(Integer orderId) {

        return orderItemRepo.findByOrderId(orderId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ==========================
    // Mapper Methods
    // ==========================

    private OrderItem mapToEntity(OrderItemRequestDTO dto) {

        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() ->
                        new ProductNotFound("Product not found"));

        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFound("Order not found"));

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity(dto.getQuantity());
        orderItem.setProduct(product);
        orderItem.setOrder(order);

        return orderItem;
    }

    private OrderItemResponseDTO mapToResponseDTO(OrderItem orderItem) {

        return new OrderItemResponseDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getOrder().getId()
        );
    }
}