package com.example.backend.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    // Constructor mặc định
    public AuthResponse() {}

    // Constructor cho lỗi
    public AuthResponse(String errorMessage) {
        this.accessToken = errorMessage;
        this.refreshToken = null;
    }

    // Constructor nhận cả access token và refresh token
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
