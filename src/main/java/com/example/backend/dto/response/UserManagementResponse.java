package com.example.backend.dto.response;

import lombok.Data;

@Data
public class UserManagementResponse {
    int id;
    String firstName;
    String lastName;
    String email;
    String department;
    String role;
}
