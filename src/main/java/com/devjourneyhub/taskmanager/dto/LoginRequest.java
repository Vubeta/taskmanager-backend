package com.devjourneyhub.taskmanager.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}