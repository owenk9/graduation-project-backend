package com.example.backend.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    // Constructor mặc định
    public AuthResponse() {}

    // Constructor nhận token
    public AuthResponse(String token) {
        this.token = token;
    }
}
