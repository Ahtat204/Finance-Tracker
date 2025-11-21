package org.asue24.financetrackerbackend.controllers;

import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 *REST Controller for managing users
 * <p>
 *     Provides endpoints to perform CRUD operations on users
 * </p>
 */
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    /**
     *Constructor for {@link UserController}
     * @param userService the service handling user-related operations
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     *return all users
     * @return {@link ResponseEntity } with a list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        var result = userService.getAllUsers();
        return ResponseEntity.ok(result);
    }

    /**
     *update a user's informations
     * @param id the unique identifier of the targeted user
     * @param user the object with new user details
     * @return the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        var updated = userService.updateUser(id, user);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     *create new user
     * @param user the user to be created
     * @return a {@link ResponseEntity} with the new created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        var saved = userService.createUser(user);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     *delete a user
     * @param id the unique identifier of the user to be deleted
     * @return true if successfully deleted , otherwise false
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }


}
