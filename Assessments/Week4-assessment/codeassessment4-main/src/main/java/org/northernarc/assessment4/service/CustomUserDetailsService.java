package org.northernarc.assessment4.service;


import lombok.RequiredArgsConstructor;
import org.northernarc.assessment4.exception.CustomerNotFoundException;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.UserRole;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + username));
        String storedPassword = customer.getPassword();
        UserRole role = customer.getRole() == null ? UserRole.USER : customer.getRole();
        String authority = "ROLE_" + role.name();
        return new User(
                customer.getEmail(),
                storedPassword,
                List.of(new SimpleGrantedAuthority(authority))
        );
    }
}
