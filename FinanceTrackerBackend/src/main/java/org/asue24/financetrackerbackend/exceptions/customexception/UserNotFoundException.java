package org.asue24.financetrackerbackend.exceptions.customexception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
    }
}

