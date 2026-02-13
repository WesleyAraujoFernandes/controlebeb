package com.controlebeb.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.controlebeb.domain.RefreshToken;
import com.controlebeb.domain.Usuario;
import com.controlebeb.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshToken createRefreshToken(Usuario usuario, String token, long duration) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(duration));

        return repository.save(refreshToken);
    }

    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expirado");
        }

        return refreshToken;
    }

    public void deleteByUser(Usuario usuario) {
        repository.deleteByUsuario(usuario);
    }
}
