package org.northernarc.unittestingdemo.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    int add(int a,int b){
        return a+b;
    }

    int subtract(int a,int b){
        return a-b;
    }

    int multiply(int a,int b){
        return a*b;
    }

    int divide(int a,int b){
        return a/b;
    }

}
