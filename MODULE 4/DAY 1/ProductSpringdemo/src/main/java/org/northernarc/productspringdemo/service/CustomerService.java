package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.CustomerRequestDTO;
import org.northernarc.productspringdemo.DTO.CustomerResponseDTO;
import org.northernarc.productspringdemo.DTO.CustomerUpdateDTO;
import org.northernarc.productspringdemo.DTO.OrderResponseDTO;

import java.util.List;

public interface CustomerService {

    CustomerResponseDTO saveCustomer(CustomerRequestDTO customerDTO);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(Integer id);

    CustomerResponseDTO updateCustomer(Integer id,
                                       CustomerUpdateDTO customerDTO);

    void deleteCustomer(Integer id);

    List<OrderResponseDTO> getCustomerOrders(Integer customerId);
}