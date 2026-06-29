package org.northernarc.jwtdemo.controller;

import org.northernarc.jwtdemo.dto.JwtRequestDTO;
import org.northernarc.jwtdemo.dto.JwtResponseDTO;
import org.northernarc.jwtdemo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/auth/login")
    public JwtResponseDTO login(@RequestBody JwtRequestDTO jwtRequest) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
                jwtRequest.getPassword()) );
        String token=jwtUtil.generateToken(jwtRequest.getUsername());
        JwtResponseDTO jwtResponse =new JwtResponseDTO();
        jwtResponse.setToken(token);
        return jwtResponse;
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String user() {
        return "Hello User!";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin!";
    }
}

