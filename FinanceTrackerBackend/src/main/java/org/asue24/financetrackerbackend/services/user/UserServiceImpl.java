package org.asue24.financetrackerbackend.services.user;

import org.asue24.financetrackerbackend.dto.CreateUserDto;
import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.exceptions.customexception.UserNotFoundException;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.asue24.financetrackerbackend.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a new {@code UserServiceImpl} with the provided {@link UserRepository}.
     *
     * @param userRepository the repository used to access {@link User} data
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userRequest the request object of the user
     * @return the {@link User} with the given id
     * @throws RuntimeException if the user is not found
     */
    @Override
    public User AuthenticateUser(UserRequestDto userRequest) throws RuntimeException {
        if (userRequest == null) throw new RuntimeException("user request is null");
        var result = userRepository.findByEmail(userRequest.getEmail().toLowerCase().trim());
        if (result.isEmpty()) throw new UserNotFoundException("email was not found");
        if (result.isPresent()) {
            var isVerified = passwordEncoder.matches(userRequest.getRawPassword(), result.get().getPassword());
            if (!isVerified) throw new UserNotFoundException("password was not correct");
        }
        return result.get();
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
    public User createUser(CreateUserDto user) throws RuntimeException {
        if (user == null) throw new RuntimeException("user cannot be null");
        var found = userRepository.findByEmail(user.email());
        if (found.isPresent()) throw new RuntimeException("user already exists");
        var hashedPassword = passwordEncoder.encode(user.password());
        var email = user.email().toLowerCase().trim();
        var storeduser = userRepository.save(new User(user.firstname(), user.lastname(), email, hashedPassword));
        userRepository.save(storeduser);
        return new User(user.firstname(), user.lastname(), email);
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
        if (email == null || email.isEmpty()) return null;
        return userRepository.findByEmail(email).orElse(null);
    }


    public void DeleteAllUsers() {
        userRepository.deleteAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
