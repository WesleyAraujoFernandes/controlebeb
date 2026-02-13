package com.controlebeb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    // private String token;
    private String accessToken;
    private String refreshToken;

    /*
     * public LoginResponse(String accessToken, String refreshToken) {
     * this.accessToken = accessToken;
     * this.refreshToken = refreshToken;
     * }
     */
    @Override
    public String toString() {
        return "LoginResponse [accessToken=" + accessToken + ", refreshToken=" + refreshToken + "]";
    }

}