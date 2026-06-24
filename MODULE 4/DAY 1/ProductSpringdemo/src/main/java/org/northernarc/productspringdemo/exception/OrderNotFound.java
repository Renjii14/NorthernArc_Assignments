package org.northernarc.productspringdemo.exception;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(String message){
        super(message);
    }
}