package org.asue24.financetrackerbackend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.websocket.AuthenticationException;
import org.asue24.financetrackerbackend.exceptions.customexception.UserNotFoundException;
import org.asue24.financetrackerbackend.exceptions.customexception.ValueNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    @Profile("dev")
    public ResponseEntity<ErrorResponse> handleUserNotFound(final UserNotFoundException ex, final HttpServletRequest request) {
        var errormessage = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errormessage);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(final AuthenticationException ex) {
        var errormessage = new ErrorResponse("AUTHENTICATION_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errormessage);
    }

    /**
     * this handler is specifically for handling missing configurations
     * @param ex
     * @return
     */
    @ExceptionHandler(ValueNotFoundException.class)
    @Profile({"prod", "dev"})
    public ResponseEntity<ErrorResponse> handleException(final ValueNotFoundException ex) {
        var errormessage = new ErrorResponse("VALUE_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errormessage);
    }
}
