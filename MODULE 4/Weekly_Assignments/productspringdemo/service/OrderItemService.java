package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.OrderItemRequestDTO;
import org.northernarc.productspringdemo.DTO.OrderItemResponseDTO;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponseDTO> getAllOrderItems();

    OrderItemResponseDTO getOrderItemById(Integer id);

    List<OrderItemResponseDTO> getItemsByOrder(Integer orderId);
}