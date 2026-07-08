package org.northernarc.week5_assess.controller;

import org.northernarc.week5_assess.dto.AuthResponse;
import org.northernarc.week5_assess.dto.LoginRequest;
import org.northernarc.week5_assess.dto.RegisterRequest;
import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(201).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}

