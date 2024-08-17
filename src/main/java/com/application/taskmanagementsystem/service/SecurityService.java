package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.UserRepository;
import com.application.taskmanagementsystem.exception.UnauthorizedException;
import com.application.taskmanagementsystem.exception.UserTakenException;
import com.application.taskmanagementsystem.model.dto.PasswordRequest;
import com.application.taskmanagementsystem.model.dto.SignInRequest;
import com.application.taskmanagementsystem.model.dto.SignUpRequest;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.security.JwtCore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SecurityService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;
  private JwtCore jwtCore;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Autowired
  public void setJwtCore(JwtCore jwtCore) {
    this.jwtCore = jwtCore;
  }

  public String register(SignUpRequest signUpRequest) {
    if (userRepository.existsUserByEmail(signUpRequest.getEmail()).booleanValue()){
      throw new UserTakenException(
          String.format("Email \"%s\" is busy ", signUpRequest.getEmail()));
    }
    else if(userRepository.existsUserByUsername(signUpRequest.getUsername()).booleanValue()){
      throw new UserTakenException(
              String.format("Nickname \"%s\" is busy ", signUpRequest.getUsername()));
    }
    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    user.setRole("ROLE_USER");
    userRepository.save(user);
    Authentication authentication = null;
    authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signUpRequest.getUsername(), signUpRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return jwtCore.generateToken(authentication);
  }

  public String login(SignInRequest signInRequest) {
    Authentication authentication = null;
    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  userRepository.findUserByEmail(signInRequest.getEmail()).get().getUsername(), signInRequest.getPassword()));
    } catch (BadCredentialsException e) {
      throw new UnauthorizedException("Wrong password or login");
    }
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return (jwtCore.generateToken(authentication));
  }

  public void changePassword(PasswordRequest passwordRequest, HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    String token;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    String username = jwtCore.getNameFromJwt(request);
    User user =
        userRepository
            .findUserByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(String.format("User '%s' not found", username)));
    user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
    userRepository.save(user);
  }
}
