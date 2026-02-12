package com.controlebeb2.controlebeb1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controlebeb2.controlebeb1.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}