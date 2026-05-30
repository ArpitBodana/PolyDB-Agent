package com.absys.io.export_data_mcp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private Integer age;

    private String city;

    private LocalDateTime createdAt;
}