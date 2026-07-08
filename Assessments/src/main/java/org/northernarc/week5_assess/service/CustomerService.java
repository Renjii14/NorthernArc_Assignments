package org.northernarc.week5_assess.service;

import java.util.Collections;
import java.util.List;

import org.northernarc.week5_assess.dto.CustomerRegistrationRequest;
import org.northernarc.week5_assess.dto.CustomerUpdateRequest;
import org.northernarc.week5_assess.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    public Customer registerCustomer(CustomerRegistrationRequest request) {
        return null;
    }

    public List<Customer> getAllCustomers() {
        return Collections.emptyList();
    }

    public Customer getCustomerById(Long id) {
        return null;
    }

    public Customer updateCustomer(Long id, CustomerUpdateRequest request) {
        return null;
    }

    public void deleteCustomer(Long id) {
    }
}

