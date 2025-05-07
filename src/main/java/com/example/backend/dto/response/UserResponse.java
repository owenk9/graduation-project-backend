package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    int id;
    private String fullName;
    private String email;
    private String role;
    String department;
}
