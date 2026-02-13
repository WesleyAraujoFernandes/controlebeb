package com.controlebeb.controller;

import com.controlebeb.domain.RefreshToken;
import com.controlebeb.domain.Usuario;
import com.controlebeb.dto.*;
import com.controlebeb.security.JwtService;
import com.controlebeb.security.UserDetailsImpl;
import com.controlebeb.service.AuthService;
import com.controlebeb.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {

        authService.logout(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(authService.refreshToken(request));
    }

}
