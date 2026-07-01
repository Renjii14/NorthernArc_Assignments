package org.northernarc.assessment4.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.northernarc.assessment4.dto.LoginRequestDTO;
import org.northernarc.assessment4.dto.LoginResponseDTO;
import org.northernarc.assessment4.exception.CustomerNotFoundException;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    // Task 8: Login endpoint - authenticate and return JWT token
    @PostMapping({"/api/auth/login", "/login"})
    @Operation(summary = "Login and get JWT token")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomerNotFoundException("User not found"));
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(jwtUtil.generateToken(customer.getEmail()));
        return response;
    }
}
