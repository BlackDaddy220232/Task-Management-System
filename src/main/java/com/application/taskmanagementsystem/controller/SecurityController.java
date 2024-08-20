package com.application.taskmanagementsystem.controller;

import com.application.taskmanagementsystem.model.dto.PasswordRequest;
import com.application.taskmanagementsystem.model.dto.SignInRequest;
import com.application.taskmanagementsystem.model.dto.SignUpRequest;
import com.application.taskmanagementsystem.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SecurityController {
  private SecurityService securityService;

  @Autowired
  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }

  @Operation(summary = "Login")
  @PostMapping("/signup")
  public ResponseEntity<String> signup(
      @Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
    String token = securityService.register(signUpRequest);
    setCookie(token, response);
    return ResponseEntity.ok("You have signed up!");
  }

  @Operation(summary = "Register")
  @PostMapping("/signin")
  public ResponseEntity<String> signin(
      @RequestBody @Valid SignInRequest signInRequest, HttpServletResponse response) {
    String token = securityService.login(signInRequest);
    setCookie(token, response);
    return ResponseEntity.ok("You have signed in!");
  }

  @Operation(summary = "Change the password")
  @PatchMapping("/editPassword")
  public ResponseEntity<String> editPassword(
      @Valid @RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
    securityService.changePassword(passwordRequest, request);
    return ResponseEntity.ok("Password was successfully updated");
  }

  void setCookie(String token, HttpServletResponse response) {
    Cookie cookie = new Cookie("token", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
