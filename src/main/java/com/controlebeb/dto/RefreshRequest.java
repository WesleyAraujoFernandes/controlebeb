package com.controlebeb.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank(message = "refreshToken é obragatório")
    private String refreshToken;
}
