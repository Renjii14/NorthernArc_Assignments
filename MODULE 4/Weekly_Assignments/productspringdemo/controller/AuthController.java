package org.northernarc.productspringdemo.controller;

import org.northernarc.productspringdemo.DTO.jwtRequestDTO;
import org.northernarc.productspringdemo.DTO.jwtResponseDTO;
import org.northernarc.productspringdemo.util.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private jwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public jwtResponseDTO login(@RequestBody jwtRequestDTO jwtRequest) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
                jwtRequest.getPassword()) );
        String token=jwtUtil.generateToken(jwtRequest.getUsername());
        jwtResponseDTO jwtResponse =new jwtResponseDTO();
        jwtResponse.setToken(token);
        return jwtResponse;
    }
}
