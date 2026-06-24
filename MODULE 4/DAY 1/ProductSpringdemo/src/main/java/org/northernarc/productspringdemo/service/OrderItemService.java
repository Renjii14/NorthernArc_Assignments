package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.OrderItemRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderItemResponseDTO;

import java.util.List;

public interface OrderItemService {

    OrderItemResponseDTO saveOrderItem(OrderItemRequestDTO dto);

    List<OrderItemResponseDTO> getAllOrderItems();

    OrderItemResponseDTO getOrderItemById(Integer id);

    void deleteOrderItem(Integer id);

    List<OrderItemResponseDTO> getItemsByOrder(Integer orderId);
}