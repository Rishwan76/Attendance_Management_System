package com.example.attendance.service;

import com.example.attendance.dto.*;
import com.example.attendance.entity.User;
import com.example.attendance.repository.*;
import com.example.attendance.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository users;
    private final StudentRepository students;
    private final JwtService jwt;

    public LoginResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        } catch (BadCredentialsException e) {
            throw new com.example.attendance.exception.ApiException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
        User u = users.findByUsername(req.username()).orElseThrow();
        UserDetails details = new org.springframework.security.core.userdetails.User(
            u.getUsername(), u.getPassword(),
            java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_"+u.getRole()))
        );
        Long studentId = students.findByUserId(u.getId()).map(s -> s.getId()).orElse(null);
        return new LoginResponse(jwt.generateToken(details, u.getRole().name(), u.getId()),
                u.getUsername(), u.getRole().name(), u.getId(), studentId);
    }
}
