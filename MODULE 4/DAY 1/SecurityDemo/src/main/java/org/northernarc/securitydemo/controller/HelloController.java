package org.northernarc.securitydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {


    @GetMapping
    public String hello(){
        return "helloworld";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String hellouser(){
        return "Hello user , you can only search product and place order!!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloadmin(){
        return "Hello admin , you can add product and delete product!!";
    }

    @GetMapping("/bye")
    public String bye(){
        return "Goodbye";
    }
    }


