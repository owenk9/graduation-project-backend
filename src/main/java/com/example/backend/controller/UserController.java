package com.example.backend.controller;

import com.example.backend.dto.request.ChangePasswordRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.CustomUserDetails;
import com.example.backend.service.UsersService;
import com.example.backend.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
            String newToken = usersService.changePassword(userDetails.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
            System.out.println("Role: " + userDetails.getRole().name());
            if (newToken == null) {
                return ResponseEntity.badRequest().body(new AuthResponse("Change password failed: Incorrect old password"));
            }
            return ResponseEntity.ok(new AuthResponse(newToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse("Change password failed: " + e.getMessage()));
        }
    }
}
