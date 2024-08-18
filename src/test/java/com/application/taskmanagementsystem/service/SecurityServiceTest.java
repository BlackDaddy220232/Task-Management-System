package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.UserRepository;
import com.application.taskmanagementsystem.exception.UnauthorizedException;
import com.application.taskmanagementsystem.exception.UserTakenException;
import com.application.taskmanagementsystem.model.dto.PasswordRequest;
import com.application.taskmanagementsystem.model.dto.SignInRequest;
import com.application.taskmanagementsystem.model.dto.SignUpRequest;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.model.enumeration.Role;
import com.application.taskmanagementsystem.security.JwtCore;
import com.application.taskmanagementsystem.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtCore jwtCore;

    @InjectMocks
    private SecurityService securityService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private PasswordRequest passwordRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password("password")
                .role(Role.EMPLOYER)
                .build();

        signInRequest = SignInRequest.builder()
                .email("testuser@example.com")
                .password("password")
                .build();

        passwordRequest = PasswordRequest.builder()
                .password("newpassword")
                .build();

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .password("encodedpassword")
                .role(Role.EMPLOYER)
                .build();
    }

    @Test
    void register_SuccessfulRegistration_ReturnsJwtToken() {
        when(userRepository.existsUserByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userRepository.existsUserByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedpassword");
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtCore.generateToken(any())).thenReturn("jwt-token");

        String result = securityService.register(signUpRequest);

        assertEquals("jwt-token", result);
        verify(userRepository).save(any(User.class));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void register_EmailAlreadyTaken_ThrowsUserTakenException() {
        when(userRepository.existsUserByEmail(signUpRequest.getEmail())).thenReturn(true);

        assertThrows(UserTakenException.class, () -> securityService.register(signUpRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_UsernameAlreadyTaken_ThrowsUserTakenException() {
        when(userRepository.existsUserByUsername(signUpRequest.getUsername())).thenReturn(true);

        assertThrows(UserTakenException.class, () -> securityService.register(signUpRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_SuccessfulLogin_ReturnsJwtToken() {
        when(userRepository.findUserByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtCore.generateToken(any())).thenReturn("jwt-token");

        String result = securityService.login(signInRequest);

        assertEquals("jwt-token", result);
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_UserNotFound_ThrowsUnauthorizedException() {
        when(userRepository.findUserByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> securityService.login(signInRequest));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_WrongPassword_ThrowsUnauthorizedException() {
        when(userRepository.findUserByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(UnauthorizedException.class, () -> securityService.login(signInRequest));
    }

    @Test
    void changePassword_SuccessfulPasswordChange() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(passwordRequest.getPassword())).thenReturn("encodednewpassword");

        securityService.changePassword(passwordRequest, request);

        verify(userRepository).save(user);
        assertEquals("encodednewpassword", user.getPassword());
    }

    @Test
    void changePassword_UserNotFound_ThrowsUsernameNotFoundException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtCore.getNameFromJwt(request)).thenReturn("testuser");
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> securityService.changePassword(passwordRequest, request));
    }
}
