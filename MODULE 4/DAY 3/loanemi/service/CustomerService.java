package org.northernarc.loanemi.service;

import org.northernarc.loanemi.dto.CreateCustomerRequest;
import org.northernarc.loanemi.model.Customer;

public interface CustomerService {

    Customer createCustomer(CreateCustomerRequest request);

    Customer getCustomer(Long customerId);
}
