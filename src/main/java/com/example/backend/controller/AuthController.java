package com.example.backend.controller;

import com.example.backend.dto.request.AuthRequest;
import com.example.backend.entity.CustomUserDetails;
import com.example.backend.service.BlacklistService; // Để blacklist token khi logout
import com.example.backend.service.CustomUserDetailsService;
import com.example.backend.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    AuthenticationManager authenticationManager;
    JwtUtil jwtUtil;
    BlacklistService blacklistService;
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest authRequest) {
        System.out.println("Login attempt: email=" + authRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Map<String, String> tokens = jwtUtil.generateTokens(userDetails);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired refresh token"));
        }

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        Map<String, String> tokens = jwtUtil.generateTokens(userDetails);
        return ResponseEntity.ok(tokens);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.addToBlacklist(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or authorization header");
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            return ResponseEntity.badRequest().body("Email not found");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        String token = jwtUtil.generateResetToken(email, customUserDetails.getId());
        customUserDetailsService.getEmailService().sendResetPasswordEmail(email, token);

        return ResponseEntity.ok("Reset password link sent to your email");
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@RequestParam String token) {
        if (jwtUtil.validateResetToken(token)) {
            return ResponseEntity.ok("Token is valid");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (!jwtUtil.validateResetToken(token)) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        Integer userId = jwtUtil.extractUserId(token);
        String email = jwtUtil.extractUsername(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null || ((CustomUserDetails) userDetails).getId() != userId) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        customUserDetails.getUser().setPassword(newPassword);
        customUserDetailsService.updateUser(customUserDetails.getUser());

        return ResponseEntity.ok("Password reset successfully");
    }
}