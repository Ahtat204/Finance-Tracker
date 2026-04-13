package org.asue24.financetrackerfrontend.model.dto;

import java.io.Serializable;

public record LoginRequest(String email, String password) implements Serializable {
}
