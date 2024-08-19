package com.application.taskmanagementsystem.aspect;

import com.application.taskmanagementsystem.model.dto.SignInRequest;
import com.application.taskmanagementsystem.model.dto.SignUpRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Component
@Slf4j
public class PointcutDefinition {
  private String username;

  @AfterReturning("com.application.taskmanagementsystem.aspect.LoggingAspect.signupPointcut()")
  public void logSignup(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    for (Object obj : args) {
      if (obj instanceof SignUpRequest signupRequest) {
        username = signupRequest.getEmail();
      }
    }
    log.info(String.format("User {%s} signed up", username));
  }

  @AfterReturning("com.application.taskmanagementsystem.aspect.LoggingAspect.signinPointcut()")
  public void logSignin(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    for (Object obj : args) {
      if (obj instanceof SignInRequest signInRequest) {
        username = signInRequest.getEmail();
      }
    }
    log.info(String.format("User {%s} signed in", username));
  }

  @AfterReturning(
      value = "com.application.taskmanagementsystem.aspect.LoggingAspect.editPasswordPointcut()",
      returning = "usernameValue")
  public void logEditPassword(JoinPoint joinPoint, String usernameValue) {
    log.info(String.format("User {%s} changed his password", usernameValue));
  }

  @Before("com.application.taskmanagementsystem.aspect.LoggingAspect.getPointcut()")
  public void logGetCall(JoinPoint joinPoint) {
    String signatureMethod = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("Method {} was called with arguments {}", signatureMethod, args);
  }
}
