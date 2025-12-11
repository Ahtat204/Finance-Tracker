package org.asue24.financetrackerbackend.exceptions;
public class ErrorResponse  {
    final long timestamp = System.currentTimeMillis();
    String error;
    String message;

    ErrorResponse(String error, String message) {
    }
}
