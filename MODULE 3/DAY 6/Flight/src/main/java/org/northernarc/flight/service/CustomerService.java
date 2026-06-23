package org.northernarc.flight.service;

import org.northernarc.flight.dto.CustomerRequestDTO;
import org.northernarc.flight.dto.CustomerResponseDTO;
import org.northernarc.flight.dto.CustomerUpdateDTO;

import java.util.List;

public interface CustomerService {

    CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO);

    CustomerResponseDTO getCustomerById(Long customerId);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO updateCustomer(Long customerId,
                                       CustomerUpdateDTO customerUpdateDTO);

    void deleteCustomer(Long customerId);
}