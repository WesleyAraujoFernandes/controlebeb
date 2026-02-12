package com.controlebeb.controlebeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controlebeb.controlebeb.domain.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
