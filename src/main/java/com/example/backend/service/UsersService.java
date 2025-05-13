package com.example.backend.service;

import com.example.backend.dto.request.EquipmentItemRequest;
import com.example.backend.dto.request.UserManagementRequest;
import com.example.backend.dto.response.EquipmentItemResponse;
import com.example.backend.dto.response.UserManagementResponse;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface UsersService{
    UserResponse findUserById(int id);
    Map<String, String> changePassword(int id, String oldPassword, String newPassword);
    UserManagementResponse addUser(UserManagementRequest userManagementRequest);
    UserManagementResponse updateUser(int id, UserManagementRequest userManagementRequest);
    void deleteUser(int id);
    Page<UserManagementResponse> getAllUsers(Pageable pageable);
}
