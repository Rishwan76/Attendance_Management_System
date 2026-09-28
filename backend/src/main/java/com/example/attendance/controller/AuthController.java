package com.example.attendance.controller;
import com.example.attendance.dto.*;
import com.example.attendance.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;
    @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest r){ return auth.login(r); }
}
