package com.application.taskmanagementsystem.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
  @Email(message = "wrong format of email")
  @NotBlank(message = "email shouldn't be null")
  private String email;
  @NotBlank(message = "password shouldn't be null")
  private String password;
}
