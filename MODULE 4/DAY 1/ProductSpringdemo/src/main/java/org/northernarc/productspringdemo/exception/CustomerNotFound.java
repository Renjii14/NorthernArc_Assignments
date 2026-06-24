package org.northernarc.productspringdemo.exception;

public class CustomerNotFound extends RuntimeException {
    public CustomerNotFound(String message){
        super(message);
    }
}
