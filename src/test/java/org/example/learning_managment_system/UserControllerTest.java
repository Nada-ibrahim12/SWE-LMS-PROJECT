package org.example.learning_managment_system;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.user;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.UserService;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private user testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new user("1", "testuser", "hashedpassword", "Student", "testuser@gmail.com");
    }

    @Test
    void testInitializeAdmin_CreatesAdminWhenNotExists() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        userService.initializeAdmin();

        verify(userRepository, times(1)).save(any(user.class));
    }

    @Test
    void testInitializeAdmin_DoesNotCreateAdminWhenExists() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));

        userService.initializeAdmin();

        verify(userRepository, never()).save(any(user.class));
    }

    @Test
    void testGetUserFromToken_Success() {
        when(jwtUtil.extractUsername("validToken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        user result = userService.getUserFromToken("validToken");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserFromToken_UserNotFound() {
        when(jwtUtil.extractUsername("invalidToken")).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserFromToken("invalidToken"));
    }

    @Test
    void testLoginUser_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtUtil.checkPassword("password", "hashedpassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "Student")).thenReturn("generatedToken");

        String token = userService.loginUser("testuser", "password");

        assertNotNull(token);
        assertEquals("generatedToken", token);
        assertTrue(testUser.isLoggedIn());
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtUtil.checkPassword("wrongpassword", "hashedpassword")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.loginUser("testuser", "wrongpassword"));
    }

    @Test
    void testLogoutUser_Success() {
        when(jwtUtil.extractUsername("validToken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        boolean result = userService.logoutUser("validToken");

        assertTrue(result);
        assertFalse(testUser.isLoggedIn());
    }

    @Test
    void testLogoutUser_UserNotFound() {
        when(jwtUtil.extractUsername("invalidToken")).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        boolean result = userService.logoutUser("invalidToken");

        assertFalse(result);
    }

    @Test
    void testHasRole_ValidRole() {
        when(jwtUtil.extractRole("validToken")).thenReturn("Instructor");

        boolean result = userService.hasRole("validToken", "Instructor");

        assertTrue(result);
    }

    @Test
    void testHasRole_InvalidRole() {
        when(jwtUtil.extractRole("validToken")).thenReturn("Student");

        boolean result = userService.hasRole("validToken", "Instructor");

        assertFalse(result);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(jwtUtil.hashPassword("password")).thenReturn("hashedpassword");

        user newUser = new user("2", "newuser", "password", "Student", "newuser@gmail.com");
        userService.registerUser(newUser);

        verify(userRepository, times(1)).save(any(user.class));
    }

   

    @Test
    void testUpdateUserProfile_Success() {
        when(jwtUtil.extractUsername("validToken")).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        user updatedProfile = new user("2", "nada", "password", "Student", "testuser@gmail.com");
        updatedProfile.setUsername("updateduser");
        updatedProfile.setPassword("newpassword");

        userService.updateUserProfile("validToken", updatedProfile);

        assertEquals("updateduser", testUser.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

}
