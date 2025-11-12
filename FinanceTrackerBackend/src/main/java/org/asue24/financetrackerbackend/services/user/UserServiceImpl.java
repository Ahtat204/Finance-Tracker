package org.asue24.financetrackerbackend.services.user;

import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link UserService} for managing {@link User} entities.
 * <p>
 * This service provides standard CRUD operations such as retrieving, creating,
 * updating, and deleting users. It delegates persistence operations to
 * {@link UserRepository}.
 * </p>
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserServiceImpl} with the provided {@link UserRepository}.
     *
     * @param userRepository the repository used to access {@link User} data
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the id of the user to retrieve
     * @return the {@link User} with the given id
     * @throws RuntimeException if the user is not found
     */
    @Override
    public User getUser(Long id) throws RuntimeException {
        return userRepository.findById(id).orElse(null);

    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the id of the user to delete
     * @return {@code true} if the user existed and was deleted, {@code false} otherwise
     */
    @Override
    public Boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all {@link User} entities
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user in the system.
     *
     * @param user the {@link User} entity to create
     * @return the created {@link User} entity with its generated id
     */
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user identified by their id.
     *
     * @param id   the id of the user to update
     * @param user the updated {@link User} data
     * @return the updated {@link User} entity
     * @throws IllegalArgumentException if the user with the specified id does not exist
     */
    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    user.setId(id); // ensure consistency
                    return userRepository.save(user);
                }).orElse(null);

    }
    @Override
    public User getUserByEmail(String email) {
        if(email == null || email.isEmpty()) return null;
        return userRepository.findByEmail(email).orElse(null);
    }
}
