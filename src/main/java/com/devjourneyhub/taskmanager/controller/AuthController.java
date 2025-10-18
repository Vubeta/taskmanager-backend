package com.devjourneyhub.taskmanager.controller;

import com.devjourneyhub.taskmanager.model.User;
import com.devjourneyhub.taskmanager.service.AuthService;
import com.devjourneyhub.taskmanager.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}