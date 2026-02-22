package org.asue24.financetrackerfrontend.model.dto.login;

import java.io.Serializable;

public record LoginRequest(String email, String password) implements Serializable {
}
