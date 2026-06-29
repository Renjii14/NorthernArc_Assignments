package org.northernarc.productspringdemo.service;

import org.northernarc.productspringdemo.DTO.CustomerRequestDTO;
import org.northernarc.productspringdemo.DTO.CustomerResponseDTO;
import org.northernarc.productspringdemo.DTO.CustomerUpdateDTO;
import org.northernarc.productspringdemo.DTO.OrderResponseDTO;
import org.northernarc.productspringdemo.exception.CustomerNotFound;
import org.northernarc.productspringdemo.model.Customer;
import org.northernarc.productspringdemo.model.Order;
import org.northernarc.productspringdemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponseDTO saveCustomer(CustomerRequestDTO customerDTO) {

        Customer customer = mapToEntity(customerDTO);

        Customer savedCustomer = customerRepo.save(customer);

        return mapToResponseDTO(savedCustomer);
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {

        return customerRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public CustomerResponseDTO getCustomerById(Integer id) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFound("Customer not found"));

        return mapToResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Integer id,
                                              CustomerUpdateDTO customerDTO) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFound("Customer not found"));

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());

        Customer updatedCustomer = customerRepo.save(customer);

        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Integer id) {

        Customer customer = customerRepo.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFound("Customer not found"));

        customerRepo.delete(customer);
    }

    @Override
    public List<OrderResponseDTO> getCustomerOrders(Integer customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFound("Customer not found"));

        return customer.getOrders()
                .stream()
                .map(this::mapToOrderResponseDTO)
                .toList();
    }

    // =======================
    // Mapper Methods
    // =======================

    private Customer mapToEntity(CustomerRequestDTO dto) {

        Customer customer = new Customer();

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setUsername(dto.getUsername());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setRole("USER");
        if (customerRepo.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(CONFLICT, "Username already exists");
        }

        return customer;
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAddress()
        );
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getOrderItems()
                        .stream()
                        .map(item -> item.getId())
                        .toList(),
                order.getStatus()
        );
    }
}