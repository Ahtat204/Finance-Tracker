package org.asue24.financetrackerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(String firstname, String lastname, @NotBlank(message = "email cannot be empty")
@Email(message = "email is mandatory", groups = UserRequestDto.class, regexp = "@") String email,
                            @NotBlank String password) {
}
