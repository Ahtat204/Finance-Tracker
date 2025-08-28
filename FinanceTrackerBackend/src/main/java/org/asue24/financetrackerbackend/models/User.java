package org.asue24.financetrackerbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private Long id;
    @Column(name = "user_name", nullable = false, length = 50)
    private String username;
    @Column(name = "email", nullable = false, length = 50, unique = true, updatable = false)
    private String email;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
