package org.asue24.financetrackerbackend.services.user;

import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**  Custom implementation of the Spring Security
 * {@link org.springframework.security.core.userdetails.UserDetailsService}.
 * This service is responsible for fetching user-specific data from the database
 * during the authentication process. */

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** * Locates the user based on the provided email address.
     *  @param email The email identifying the user whose data is required.
     *  @return A fully populated {@link UserDetails} object required for authentication.
     *  @throws UsernameNotFoundException If a user with the specified email cannot be found. */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new User(user.getEmail(),user.getPassword(),new ArrayList<>());

    }
}
