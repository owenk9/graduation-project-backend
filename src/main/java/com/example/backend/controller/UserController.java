package com.example.backend.controller;

import com.example.backend.dto.request.ChangePasswordRequest;
import com.example.backend.dto.request.UserManagementRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.UserManagementResponse;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.CustomUserDetails;
import com.example.backend.service.UsersService;
import com.example.backend.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UsersService usersService;
    JwtUtil jwtUtil;
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserResponse userResponse = new UserResponse(
                    userDetails.getId(),
                    userDetails.getFullName(),
                    userDetails.getUsername(),
                    userDetails.getRole().name(),
                    userDetails.getDepartment()
            );
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new UserResponse(0,"Unknown","unknown@example.com", "USER", "Unknown" ));
        }
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        UserResponse userResponse = usersService.findUserById(id);
        return ResponseEntity.ok(userResponse);
    }
    @PatchMapping("/change_password")
    public ResponseEntity<AuthResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map<String, String> tokens = usersService.changePassword(userDetails.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
            if (tokens == null || tokens.get("accessToken") == null) {
                return ResponseEntity.badRequest().body(new AuthResponse("Change password failed: Incorrect old password"));
            }
            return ResponseEntity.ok(new AuthResponse(tokens.get("accessToken"), tokens.get("refreshToken")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Change password failed: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<UserManagementResponse> addUser(@RequestBody UserManagementRequest userManagementRequest) {
        UserManagementResponse userManagementResponse = usersService.addUser(userManagementRequest);
        return ResponseEntity.ok(userManagementResponse);
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<UserManagementResponse> updateUser(@PathVariable int id, @RequestBody UserManagementRequest userManagementRequest) {
        UserManagementResponse userManagementResponse = usersService.updateUser(id, userManagementRequest);
        return ResponseEntity.ok(userManagementResponse);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/get")
    public ResponseEntity<Page<UserManagementResponse>> getUsers(
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserManagementResponse> userPage;
        userPage = usersService.getAllUsers(pageable);
        return ResponseEntity.ok(userPage);
    }
    @GetMapping("/search")
    public ResponseEntity<Page<UserManagementResponse>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserManagementResponse> userPage = usersService.searchUsersByName(name, pageable);
        return ResponseEntity.ok(userPage);
    }
}
