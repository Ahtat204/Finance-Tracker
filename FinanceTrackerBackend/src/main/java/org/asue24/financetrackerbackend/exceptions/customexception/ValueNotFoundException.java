package org.asue24.financetrackerbackend.exceptions.customexception;

public class ValueNotFoundException extends RuntimeException {
    public ValueNotFoundException(String message) {
        super(message);
    }
}
