package com.controlebeb.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}
