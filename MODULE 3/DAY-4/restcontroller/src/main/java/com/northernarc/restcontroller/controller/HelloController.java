package com.northernarc.restcontroller.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String  hello(){
        return "Hello World";
    }


    @RequestMapping("")
    public String welcome(){
        return "Welcome to Springboot!!!";
    }

    @RequestMapping("/bye")
    public String bye(){
        return "See you SOON!!!!";
    }


}
