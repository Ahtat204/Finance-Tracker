package org.asue24.financetrackerfrontend.model.dto.login;

import java.io.Serializable;

public record LoginResponse(int status, String message) implements Serializable {
}
