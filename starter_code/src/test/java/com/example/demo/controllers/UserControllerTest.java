package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.*;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void init(){
        userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
/*        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);*/

    }
    private ResponseEntity<User> createUser(){
        when(bCryptPasswordEncoder.encode("test123")).thenReturn("hashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("arwa");
        request.setPassword("test123");
        request.setConfirmPassword("test123");

        final ResponseEntity<User> response = userController.createUser(request);
        return response;
    }

    @Test
    public void create_user(){
        ResponseEntity<User> userResponse = createUser();
        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());

        User user = userResponse.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("arwa", user.getUsername());
        assertEquals("hashed", user.getPassword());
    }

    @Test
    public void find_user_ById() throws Exception {
        ResponseEntity<User> createUserResponse = createUser();
        when(userRepository.findById(createUserResponse.getBody().getId())).thenReturn(Optional.of(createUserResponse.getBody()));
        final ResponseEntity<User> user = userController.findById(createUserResponse.getBody().getId());
        assertEquals(createUserResponse.getBody().getUsername(), user.getBody().getUsername());
        assertTrue(createUserResponse.getBody().getId() == user.getBody().getId());
        assertEquals(200, user.getStatusCodeValue());
    }

    @Test
    public void find_user_ByUsername(){
        ResponseEntity<User> createUserResponse = createUser();
        when(userRepository.findByUsername(createUserResponse.getBody().getUsername())).thenReturn(createUserResponse.getBody());
        final ResponseEntity<User> user = userController.findByUserName(createUserResponse.getBody().getUsername());
        assertEquals(createUserResponse.getBody().getUsername(), user.getBody().getUsername());
        assertTrue(createUserResponse.getBody().getId() == user.getBody().getId());
        assertEquals(200, user.getStatusCodeValue());

    }

    @Test
    public void create_user_with_invalid_password(){
        when(bCryptPasswordEncoder.encode("test123")).thenReturn("hashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("arwa");
        request.setPassword("test");
        request.setConfirmPassword("test123");
        final ResponseEntity<User> response = userController.createUser(request);
        assertEquals(400, response.getStatusCodeValue());
    }
}
