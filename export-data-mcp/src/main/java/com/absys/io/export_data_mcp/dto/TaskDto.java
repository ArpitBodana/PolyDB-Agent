package com.absys.io.export_data_mcp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private String status;

    private String priority;

    private String assignedTo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
