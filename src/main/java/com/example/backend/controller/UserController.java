package com.example.backend.controller;

import com.example.backend.dto.response.UserResponse;
import com.example.backend.entity.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserResponse userResponse = new UserResponse(
                    userDetails.getFullName(),
                    userDetails.getUsername(),
                    userDetails.getRole()
            );
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new UserResponse("Unknown","unknown@example.com", "USER" ));
        }
    }
}
