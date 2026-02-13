package com.controlebeb.service;

import com.controlebeb.domain.RefreshToken;
import com.controlebeb.domain.Usuario;
import com.controlebeb.dto.*;
import com.controlebeb.exception.ResourceNotFoundException;
import com.controlebeb.exception.UnauthorizedException;
import com.controlebeb.repository.RefreshTokenRepository;
import com.controlebeb.repository.UsuarioRepository;
import com.controlebeb.security.*;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Transactional
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenService.deleteByUser(userDetails.getUsuario());

        refreshTokenService.createRefreshToken(
                userDetails.getUsuario(),
                refreshToken,
                refreshExpiration);
        LoginResponse response = new LoginResponse(accessToken, refreshToken);
        System.out.println(response);
        return response;
    }

    @Transactional
    public LoginResponse refreshToken(RefreshRequest request) {

        String requestRefreshToken = request.getRefreshToken();

        // 1️⃣ validar assinatura + expiração
        if (!jwtService.isTokenValid(requestRefreshToken)) {
            throw new RuntimeException("Refresh token inválido");
        }

        // 2️⃣ extrair usuário
        String email = jwtService.extractUsername(requestRefreshToken);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 3️⃣ verificar se token existe no banco
        RefreshToken storedToken = refreshTokenRepository
                .findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token não encontrado"));

        // 4️⃣ verificar expiração no banco
        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token expirado");
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

        // 5️⃣ gerar novo access token
        String newAccessToken = jwtService.generateToken(userDetails);

        // 6️⃣ gerar novo refresh token (ROTAÇÃO)
        refreshTokenRepository.delete(storedToken);

        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        RefreshToken newEntity = RefreshToken.builder()
                .token(newRefreshToken)
                .usuario(usuario)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        if (newEntity == null) {
            throw new RuntimeException("Refresh token inválido");
        }
        refreshTokenRepository.save(newEntity);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {

        // 1️⃣ validar assinatura
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new UnauthorizedException("Refresh token inválido");
        }

        // 2️⃣ buscar no banco
        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token não encontrado"));

        // 3️⃣ remover (revogação)
        if (storedToken != null)
            refreshTokenRepository.delete(storedToken);
    }

}
