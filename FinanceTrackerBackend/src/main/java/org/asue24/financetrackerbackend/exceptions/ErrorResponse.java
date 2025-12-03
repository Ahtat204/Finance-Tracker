package org.asue24.financetrackerbackend.exceptions;

import java.io.Serializable;

public class ErrorResponse implements Serializable {
    final long timestamp = System.currentTimeMillis();
    String error;
    String message;

    ErrorResponse(String error, String message) {
    }
}
