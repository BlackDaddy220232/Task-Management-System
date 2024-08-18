package com.application.taskmanagementsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {
    @NotBlank(message = "Comment should`be null")
    @Size(max = 50)
    private String content;
}
