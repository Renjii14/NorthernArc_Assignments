package org.northernarc.productrental.exception;

public class RentalRecordNotFoundException extends RuntimeException {
    public RentalRecordNotFoundException(Long id) {
        super("Rental record not found with id: " + id);
    }
}
