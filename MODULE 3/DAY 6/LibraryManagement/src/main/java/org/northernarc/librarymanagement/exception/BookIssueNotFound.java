package org.northernarc.librarymanagement.exception;

public class BookIssueNotFound extends RuntimeException {
    public BookIssueNotFound(String message) {
        super(message);
    }
}
