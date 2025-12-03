package org.asue24.financetrackerbackend.services.user;

import org.asue24.financetrackerbackend.dto.UserRequestDto;
import org.asue24.financetrackerbackend.entities.User;
import org.asue24.financetrackerbackend.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    User user = new User("lahcen", "test", "test", "test");
    UserRequestDto userRequestDto = new UserRequestDto("test", "test");
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //the happy path tests

    /// ////////////////////////////////////////
    @Test
    void authenticateUserByEmail_returnUser() {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(user));
        var result = userServiceImpl.AuthenticateUser(userRequestDto);
        Assertions.assertNotNull(result);
    }

    @Test
    void deleteUser_returnTrueIfUserDeleted() {
        when(userRepository.existsById(23L)).thenReturn(true);
        var result = userServiceImpl.deleteUser(23L);
        Assertions.assertEquals(true, result);
        verify(userRepository, times(1)).deleteById(23L);
    }

    @Test
    void getAllUsers_returnAllUsers() {
        var users = List.of(new User("tes4", "test", "test", "test"), new User("lahcen", "test2", "test2", "test2"));
        when(userRepository.findAll()).thenReturn(users);
        var result = userServiceImpl.getAllUsers();
        Assertions.assertEquals(users, result);
    }

    @Test
    void createUser_returnUserCreated() {
        when(userRepository.save(user)).thenReturn(user);
        // var result = userServiceImpl.createUser(user);
        // Assertions.assertEquals(user, result);
    }

    @Test
    void updateUser_returnUserUpdated() {
        when(userRepository.findById(23L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        var result = userServiceImpl.updateUser(23L, user);
        Assertions.assertEquals(user, result);
        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getFirstname(), result.getFirstname());
        verify(userRepository).findById(23L);
        verify(userRepository).save(user);
    }

    /// //////////////////////////////
    //the edge cases
    @Test
    void deleteUser_returnFalseIfUserNotDeleted() {
        when(userRepository.existsById(22L)).thenReturn(false);
        var result = userServiceImpl.deleteUser(22L);
        Assertions.assertEquals(false, result);
    }

    @Test
    void authenticateUserByEmail_returnNullIfUserNotFound() {
        when(userRepository.findById(23L)).thenReturn(Optional.empty());
        var result = userServiceImpl.AuthenticateUser(userRequestDto);
        Assertions.assertEquals(null, result);
    }

    @Test
    public void GetAllUsers_returnEmptyListIfUsersNotFound() {
        var users = new ArrayList();
        when(userRepository.findAll()).thenReturn(users);
        var result = userServiceImpl.getAllUsers();
        Assertions.assertEquals(users, result);
        Assertions.assertEquals(users.size(), 0);
        Assertions.assertNotNull(result);
    }


}