package com.application.taskmanagementsystem.config;

import java.util.Arrays;
import java.util.List;

import com.application.taskmanagementsystem.security.TokenFilter;
import com.application.taskmanagementsystem.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@Data
public class SecurityConfigurator {
  private TokenFilter tokenFilter;
  private UserService userService;
  private MyAuthenticationEntryPoint authenticationEntryPoint;

  public static final String SIGNUP_URL = "/auth/signup";
  public static final String SIGNIN_URL = "/auth/signin";
  public static final String EDIT_PASSWORD_URL = "/auth/editPassword";
  public static final String USER_TASK_URL = "/user/task";
  public static final String USER_TASK_EMPLOYEE_URL = "/user/task/employee";
  public static final String ALL_EMPLOYEES_URL = "/user/allEmployees";
  public static final String ALL_TASKS_URL = "/task/all";
  public static final String USER_TASKS_URL = "/user/tasks";
  public static final String USER_TASKS_BY_ID_URL = "/user/{id}/tasks";
  public static final String TASK_COMMENT_URL = "/user/task/{id}/comment";
  public static final String TASK_STATUS_URL = "/user/task/{id}/status";
  public static final String TASK_EDIT_URL = "/user/task/{id}";
  public static final String TASK_DELETE_URL = "/user/task/{id}";
  public static final String EMPLOYER="EMPLOYER";
  public static final HttpMethod POST_METHOD = HttpMethod.POST;
  public static final HttpMethod PATCH_METHOD = HttpMethod.PATCH;
  public static final HttpMethod GET_METHOD = HttpMethod.GET;
  public static final HttpMethod DELETE_METHOD = HttpMethod.DELETE;

  @Autowired
  public void setMyAuthenticationEntryPoint(MyAuthenticationEntryPoint authenticationEntryPoint) {
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setTokenFilter(TokenFilter tokenFilter) {
    this.tokenFilter = tokenFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  @Primary
  public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(
      AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(
            cors ->
                cors.configurationSource(
                    request -> {
                      CorsConfiguration configuration = new CorsConfiguration();
                      configuration.setAllowedOrigins(List.of("http://localhost:8080"));
                      configuration.setAllowedMethods(
                          Arrays.asList("GET", "POST", "PUT", "DELETE")); // Разрешенные методы
                      configuration.setAllowedHeaders(
                          Arrays.asList("Authorization", "Content-Type")); // Разрешенные заголовки
                      configuration.setAllowCredentials(true); // Разрешение использования куки
                      return configuration;
                    }))
        .exceptionHandling(
            exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(POST_METHOD, SIGNUP_URL, SIGNIN_URL)
                    .permitAll()
                    .requestMatchers(PATCH_METHOD, EDIT_PASSWORD_URL)
                    .permitAll()
                    .requestMatchers(POST_METHOD, USER_TASK_URL)
                    .hasAuthority(EMPLOYER)
                    .requestMatchers(POST_METHOD, USER_TASK_EMPLOYEE_URL)
                    .hasAuthority(EMPLOYER)
                    .requestMatchers(GET_METHOD, ALL_EMPLOYEES_URL)
                    .hasAuthority(EMPLOYER)
                    .requestMatchers(GET_METHOD, ALL_TASKS_URL)
                    .fullyAuthenticated()
                    .requestMatchers(GET_METHOD, USER_TASKS_URL)
                    .fullyAuthenticated()
                    .requestMatchers(GET_METHOD, USER_TASKS_BY_ID_URL)
                    .fullyAuthenticated()
                    .requestMatchers(POST_METHOD, TASK_COMMENT_URL)
                    .fullyAuthenticated()
                    .requestMatchers(PATCH_METHOD, TASK_STATUS_URL)
                    .fullyAuthenticated()
                    .requestMatchers(PATCH_METHOD, TASK_EDIT_URL)
                    .hasAuthority(EMPLOYER)
                    .requestMatchers(DELETE_METHOD, TASK_DELETE_URL)
                    .hasAuthority(EMPLOYER))
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
