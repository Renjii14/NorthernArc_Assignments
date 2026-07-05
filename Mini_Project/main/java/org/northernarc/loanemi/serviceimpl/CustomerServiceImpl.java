package org.northernarc.loanemi.serviceimpl;

import org.northernarc.loanemi.dto.CreateCustomerRequest;
import org.northernarc.loanemi.exception.CustomerNotFoundException;
import org.northernarc.loanemi.model.Customer;
import org.northernarc.loanemi.repository.CustomerRepository;
import org.northernarc.loanemi.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer for email={} city={}", request.getEmail(), request.getCity());
        if (customerRepository.existsByEmail(request.getEmail())) {
            log.warn("Duplicate customer creation attempted for email={}", request.getEmail());
            throw new DataIntegrityViolationException("Email already registered");
        }

        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setCity(request.getCity());
        customer.setCreditScore(request.getCreditScore());
        customer.setRole(request.getRole());
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully customerId={} email={}",
                savedCustomer.getCustomerId(), savedCustomer.getEmail());
        return savedCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomer(Long customerId) {
        log.info("Fetching customer details customerId={}", customerId);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
    }
}
