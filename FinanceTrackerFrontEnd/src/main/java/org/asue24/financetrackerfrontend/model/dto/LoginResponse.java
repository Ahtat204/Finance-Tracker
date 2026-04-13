package org.asue24.financetrackerfrontend.model.dto;

import java.io.Serializable;


public record LoginResponse(String jwtToken,String email) implements Serializable {}
