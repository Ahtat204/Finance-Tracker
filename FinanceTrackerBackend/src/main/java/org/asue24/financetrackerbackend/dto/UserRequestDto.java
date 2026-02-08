package org.asue24.financetrackerbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used to securely transfer user authentication
 * credentials (email and password) between the client and the server
 * during a login or authentication request.
 * <p>
 * This class uses Lombok annotations to automatically generate getters, setters,
 * constructors, and standard utility methods, reducing boilerplate code.
 * </p>
 *
 * @author asue24
 * @version 1.0
 * @since 2025-11-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    /**
     * The email address provided by the user for authentication.
     * This field serves as the primary identifier for the user's account.
     */
    @NotBlank
    @Email(message = "email is mandatory", groups = UserRequestDto.class, regexp = "@")
    private String email;
    /**
     * The password provided by the user for authentication.
     * This field should be securely handled and never exposed in logs or insecure environments.
     */
    @NotBlank(message = "password cannot be null")
    private String rawPassword;
}