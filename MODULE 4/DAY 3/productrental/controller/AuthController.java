package org.northernarc.productrental.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.northernarc.productrental.dto.LoginRequestDTO;
import org.northernarc.productrental.dto.LoginResponseDTO;
import org.northernarc.productrental.model.Customer;
import org.northernarc.productrental.repository.CustomerRepository;
import org.northernarc.productrental.security.JwtUtil;
import org.northernarc.productrental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(jwtUtil.generateToken(customer.getEmail()));
        return response;
    }
}
