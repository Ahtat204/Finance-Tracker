package org.asue24.financetrackerbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents an application user who owns accounts.
 * A user has a unique ID, username, email, and password.
 *
 * <p>Each user may have multiple accounts associated
 * with them. The email is unique and immutable once created.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Users")
@Table(name = "Users")
public class User {

    /** Unique identifier for the user (primary key). */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private Long id;

    /** The username chosen by the user. */
    @Column(name = "user_name", nullable = false, length = 50)
    private String username;

    /** The unique email address of the user (cannot be updated after creation). */
    @Column(name = "email", nullable = false, length = 50, unique = true, updatable = false)
    private String email;

    /** The user's password (should be stored in a hashed form). */
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    /**
     * Constructs a new User with the given details.
     *
     * @param username the username
     * @param email the email address
     * @param password the password
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

