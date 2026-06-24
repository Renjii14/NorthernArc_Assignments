package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.OrderRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderResponseDTO;
import org.northernarc.productspringdemo.DTO.OrderUpdateDTO;
import org.northernarc.productspringdemo.exception.CustomerNotFound;
import org.northernarc.productspringdemo.exception.OrderNotFound;
import org.northernarc.productspringdemo.exception.ProductNotFound;
import org.northernarc.productspringdemo.model.Customer;
import org.northernarc.productspringdemo.model.Order;
import org.northernarc.productspringdemo.model.OrderItem;
import org.northernarc.productspringdemo.model.Product;
import org.northernarc.productspringdemo.repository.CustomerRepository;
import org.northernarc.productspringdemo.repository.OrderItemRepository;
import org.northernarc.productspringdemo.repository.OrderRepository;
import org.northernarc.productspringdemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFound("Product not found"));

        Order savedOrder;

        if (request.getOrderId() == null) {

            Customer customer = customerRepo
                    .findById(request.getCustomerId())
                    .orElseThrow(() ->
                            new CustomerNotFound("Customer not found"));

            Order order = new Order();
            order.setCustomer(customer);
            order.setStatus("PENDING");

            savedOrder = orderRepo.save(order);

        } else {

            savedOrder = orderRepo.findById(request.getOrderId())
                    .orElseThrow(() ->
                            new OrderNotFound("Order not found"));
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(savedOrder);
        orderItem.setProduct(product);
        orderItem.setQuantity(request.getQuantity());

        orderItemRepo.save(orderItem);

        return mapToResponseDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO getOrderById(Integer id) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFound("Order not found"));

        return mapToResponseDTO(order);
    }

    @Override
    public void cancelOrder(Integer orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFound("Order not found"));

        orderRepo.delete(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(Integer customerId) {

        return orderRepo.findOrderByCustomerId(customerId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO updateOrder(Integer id,
                                        OrderUpdateDTO dto) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFound("Order not found"));

        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }

        Order updatedOrder = orderRepo.save(order);

        return mapToResponseDTO(updatedOrder);
    }

    // =========================
    // Mapper Methods
    // =========================

    private OrderResponseDTO mapToResponseDTO(Order order) {

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getOrderItems()
                        .stream()
                        .map(OrderItem::getId)
                        .toList(),
                order.getStatus()
        );
    }
}