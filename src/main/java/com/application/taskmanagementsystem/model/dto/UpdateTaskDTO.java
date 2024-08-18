package com.application.taskmanagementsystem.model.dto;

import com.application.taskmanagementsystem.model.enumeration.Priority;
import com.application.taskmanagementsystem.model.enumeration.Status;
import lombok.Data;

@Data
public class UpdateTaskDTO {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
}
