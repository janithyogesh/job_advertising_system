package com.jobportal.controller;

import com.jobportal.dto.AuthRequest;
import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.model.UserAccount;
import com.jobportal.security.JwtService;
import com.jobportal.service.PortalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final PortalService portalService;
    private final JwtService jwtService;

    public AuthController(PortalService portalService, JwtService jwtService) {
        this.portalService = portalService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserAccount user = portalService.register(
                    request.getFullName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getRole()
            );
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            UserAccount user = portalService.authenticate(request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
