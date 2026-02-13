package com.controlebeb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "deve ser um email válido")
    @NotBlank(message = "não pode estar em branco")
    private String email;

    @NotBlank(message = "não pode estar em branco")
    private String senha;
}
