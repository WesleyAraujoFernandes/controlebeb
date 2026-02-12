package com.controlebeb.controlebeb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "empresa")
public class Empresa extends BaseEntity {
    @Column(nullable = false, length = 150)
    private String nome;
    @Column(nullable = false, length = 50)
    private String plano;
    @Column(nullable = false, length = 50)
    private String status;

}
