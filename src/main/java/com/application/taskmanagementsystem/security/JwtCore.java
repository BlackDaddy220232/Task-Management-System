package com.application.taskmanagementsystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtCore {
  @Value("${Weather.app.secret}")
  private String secret;

  @Value("${Weather.app.lifetime}")
  private int lifetime;

  public String generateToken(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userDetails.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + lifetime))
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public String getTokenFromRequest(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          return cookie.getValue();
        }
      }
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access token not found in cookies");
  }

  public String getNameFromJwt(HttpServletRequest request) {
    String token = getTokenFromRequest(request);
    return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
  }
}
