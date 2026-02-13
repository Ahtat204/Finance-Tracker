package org.asue24.financetrackerbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private Long id;

    /** The firstname of the user. */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstname;

    /** The lastname of the user. */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastname;

    /** The unique email address of the user (cannot be updated after creation). */
    @Column(name = "email", nullable = false, length = 50, unique = true, updatable = false)
    private String email;

    /** The user's password (should be stored in a hashed form). */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Account> accounts;
    /**
     * Constructs a new User with the given details.
     *
     * @param firstname the username
     * @param email the email address
     * @param password the password
     */
    public User(String firstname, String lastname,String email, String password) {
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.lastname = lastname;
    }
    public User(Long id,String firstname, String lastname,String email, String password) {
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.lastname = lastname;
        this.id = id;
    }
    public User(String firstname, String lastname,String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }
    public User(Long id){
        this.id = id;
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

