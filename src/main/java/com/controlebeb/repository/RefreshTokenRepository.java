package com.controlebeb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controlebeb.domain.RefreshToken;
import com.controlebeb.domain.Usuario;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsuario(Usuario usuario);
}
