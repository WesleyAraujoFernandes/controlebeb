package com.controlebeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controlebeb.domain.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
