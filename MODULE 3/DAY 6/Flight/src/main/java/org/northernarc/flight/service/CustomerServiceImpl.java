package org.northernarc.flight.service;

import org.northernarc.flight.dto.CustomerRequestDTO;
import org.northernarc.flight.dto.CustomerResponseDTO;
import org.northernarc.flight.dto.CustomerUpdateDTO;
import org.northernarc.flight.exception.CustomerNotFound;
import org.northernarc.flight.model.Customer;
import org.northernarc.flight.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;


    @Override
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer=mapToEntity(customerRequestDTO);
        Customer savedCustomer=customerRepo.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long customerId) {
        Customer customer=customerRepo.findById(customerId).orElseThrow(()->new CustomerNotFound("Customer Not Found"));
        return mapToResponseDTO(customer);

    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        List<Customer> customers=customerRepo.findAll();
        return customers.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long customerId, CustomerUpdateDTO customerUpdateDTO) {
        Customer existingCustomer=customerRepo.findById(customerId).orElseThrow(()->new CustomerNotFound("Customer Not Found"));
        existingCustomer.setFirstName(customerUpdateDTO.getFirstName());
        existingCustomer.setLastName(customerUpdateDTO.getLastName());
        existingCustomer.setEmail(customerUpdateDTO.getEmail());
        existingCustomer.setMobileNumber(customerUpdateDTO.getMobileNumber());
        Customer updatedCustomer=customerRepo.save(existingCustomer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer=customerRepo.findById(customerId).orElseThrow(()->new CustomerNotFound("Customer Not Found"));
        customerRepo.delete(customer);

    }

    private Customer mapToEntity(CustomerRequestDTO customerRequestDTO){
        Customer customer=new Customer();
        customer.setFirstName(customerRequestDTO.getFirstName());
        customer.setLastName(customerRequestDTO.getLastName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setMobileNumber(customerRequestDTO.getMobileNumber());
        return customer;
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer){
        CustomerResponseDTO dto=new CustomerResponseDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        return dto;
    }
}
