package com.application.taskmanagementsystem.model.dto;

import com.application.taskmanagementsystem.model.enumeration.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusDTO {
    @NotNull(message = "status shouldn`t be null")
    private Status status;
}
