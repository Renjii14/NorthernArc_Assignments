package org.northernarc.librarymanagement.ControllerAdvice;

import org.northernarc.librarymanagement.exception.BookIssueNotFound;
import org.northernarc.librarymanagement.exception.BookNotFound;
import org.northernarc.librarymanagement.exception.FineNotFound;
import org.northernarc.librarymanagement.exception.LibrarianNotFound;
import org.northernarc.librarymanagement.exception.MemberNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFound.class)
    public ResponseEntity<Map<String, String>> handleBookNotFound(BookNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(MemberNotFound.class)
    public ResponseEntity<Map<String, String>> handleMemberNotFound(MemberNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(LibrarianNotFound.class)
    public ResponseEntity<Map<String, String>> handleLibrarianNotFound(LibrarianNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(BookIssueNotFound.class)
    public ResponseEntity<Map<String, String>> handleBookIssueNotFound(BookIssueNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(FineNotFound.class)
    public ResponseEntity<Map<String, String>> handleFineNotFound(FineNotFound e) {
        return ResponseEntity.status(404)
                .body(Map.of("Message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity.status(400).body(errors);
    }
}
