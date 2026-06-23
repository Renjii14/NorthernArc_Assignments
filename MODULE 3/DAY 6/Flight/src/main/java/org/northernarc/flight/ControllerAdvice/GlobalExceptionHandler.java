package org.northernarc.flight.ControllerAdvice;
import org.northernarc.flight.exception.CustomerNotFound;
import org.northernarc.flight.exception.FlightNotFound;
import org.northernarc.flight.exception.TicketNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFound.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFound(CustomerNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(FlightNotFound.class)
    public ResponseEntity<Map<String, String>> handleFlightNotFound(FlightNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(TicketNotFound.class)
    public ResponseEntity<Map<String, String>> handleTicketNotFound(TicketNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(400).body(errors);
    }
}
