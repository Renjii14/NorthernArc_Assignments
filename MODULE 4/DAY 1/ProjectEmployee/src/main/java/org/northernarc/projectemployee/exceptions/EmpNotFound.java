package org.northernarc.projectemployee.exceptions;

public class EmpNotFound extends RuntimeException{
    public EmpNotFound(String message) {
        super(message);
    }
}
