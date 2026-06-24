package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.OrderRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderResponseDTO;
import org.northernarc.productspringdemo.DTO.OrderUpdateDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO placeOrder(OrderRequestDTO request);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Integer id);

    void cancelOrder(Integer orderId);

    List<OrderResponseDTO> getOrdersByCustomer(Integer customerId);

    OrderResponseDTO updateOrder(Integer id,
                                 OrderUpdateDTO dto);
}