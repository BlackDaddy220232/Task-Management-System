package com.application.taskmanagementsystem.model.dto;

import com.application.taskmanagementsystem.model.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {
  @NotBlank(message = "username shouldn't be null")
  private String username;
  @Email(message = "wrong format of email")
  @NotBlank(message = "email shouldn't be null")
  private String email;
  @NotBlank(message = "password shouldn't be null")
  private String password;
  @NotNull(message = "Role shouldn't be null")
  private Role role;
}
