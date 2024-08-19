package com.application.taskmanagementsystem.controller;

import com.application.taskmanagementsystem.model.dto.PasswordRequest;
import com.application.taskmanagementsystem.model.dto.SignInRequest;
import com.application.taskmanagementsystem.model.dto.SignUpRequest;
import com.application.taskmanagementsystem.service.SecurityService;
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

  @PostMapping("/signup")
  ResponseEntity<String> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(securityService.register(signUpRequest));
  }

  @PostMapping("/signin")
  public String signin(
      @RequestBody @Valid SignInRequest signInRequest, HttpServletResponse response) {
    String token = securityService.login(signInRequest);
    Cookie cookie = new Cookie("token", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    response.addCookie(cookie);
    return token; // Delete this in relies version
  }

  @PatchMapping("/editPassword")
  public ResponseEntity<String> editPassword(
      @Valid @RequestBody PasswordRequest passwordRequest, HttpServletRequest request) {
    securityService.changePassword(passwordRequest, request);
    return ResponseEntity.ok("Password was successfully updated");
  }
}
