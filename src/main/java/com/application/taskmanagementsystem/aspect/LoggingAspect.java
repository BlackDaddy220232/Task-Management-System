package com.application.taskmanagementsystem.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(* com.application.taskmanagementsystem.controller.SecurityController.signup(..))")
    public void signupPointcut() {}

    @Pointcut("execution(* com.application.taskmanagementsystem.controller.SecurityController.signin(..))")
    public void signinPointcut() {}

    @Pointcut("execution(* com.application.taskmanagementsystem.service.SecurityService.changePassword(..))")
    public void editPasswordPointcut() {}

    @Pointcut("execution(* com.application.taskmanagementsystem.controller.*.*(..))")
    public void getPointcut() {}

}
