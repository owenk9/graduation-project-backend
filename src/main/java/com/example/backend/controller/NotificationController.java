package com.example.backend.controller;

import com.example.backend.dto.request.NotificationRequest;
import com.example.backend.dto.response.NotificationResponse;
import com.example.backend.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request.getUsersId(), request.getMessage());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@PathVariable int userId) {
        List<NotificationResponse> responses = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(responses);
    }
}
