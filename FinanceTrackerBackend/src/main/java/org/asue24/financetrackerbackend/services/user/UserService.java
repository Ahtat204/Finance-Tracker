package org.asue24.financetrackerbackend.services.user;

import org.asue24.financetrackerbackend.entities.User;

import java.util.List;

/**
 * User Interface for managing {@link User} entities
 * <p>
 * Provides CRUD operations and business logic related to users
 * the service layer acts as a bridge between the Controller layer and the persistence layer(repository)
 * </p>
 */
public interface UserService {

    /**
     * Retrieves an {@link User} by its unique identifier
     *
     * @param id the unique identifier of the user
     * @return the {@link User} if found, or {@code RunTimeException} if no account exists with the given ID
     */
    User getUser(Long id) throws RuntimeException;

    /**
     * Deletes an existing {@link User}.
     *
     * @param id the user id to be deleted
     * @return {@code true} if the user was successfully deleted,
     * *         {@code false} otherwise
     */
    Boolean deleteUser(Long id);

    /**
     * Retrieves all {@link User} entities in the system.
     *
     * @return a list of all users; may be empty if no accounts exist
     */
    List<User> getAllUsers();

    /**
     * create a new {@link User}
     *
     * @param user the user object to be created
     * @return returns the created user
     */
    User createUser(User user);

    /**
     * updates the details of an existing {@link User}
     *
     * @param id   the unique identifier of the user
     * @param user the user object with the new details
     * @return the new and updated User Object
     */
    User updateUser(Long id, User user);
}
