package com.application.taskmanagementsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PasswordRequest {
  @NotBlank(message = "password shouldn`t be null")
  private String password;
}
