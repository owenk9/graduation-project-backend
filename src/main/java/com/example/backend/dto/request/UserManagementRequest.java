package com.example.backend.dto.request;

import lombok.Data;

@Data
public class UserManagementRequest {
    String firstName;
    String lastName;
    String email;
    String password;
    String department;
    String role;
}
