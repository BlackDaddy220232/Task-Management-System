package com.application.taskmanagementsystem.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {
  private String username;
  private String password;
  private String country;

}
