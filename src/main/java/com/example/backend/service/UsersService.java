package com.example.backend.service;

import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersService{
    UserResponse findUserById(int id);
    String changePassword(int id, String oldPassword, String newPassword);
}
