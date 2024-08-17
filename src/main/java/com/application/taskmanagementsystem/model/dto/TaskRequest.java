package com.application.taskmanagementsystem.model.dto;

import com.application.taskmanagementsystem.model.enumeration.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    @NotBlank(message = "title should`t be null")
    String title;
    @Size(max = 200, message = ("definition is too big"))
    @NotBlank(message = "definition should`t be null")
    String definition;
    @NotNull(message = "priority should`t be null")
    @Enumerated(EnumType.ORDINAL)
    Priority priority;


}
