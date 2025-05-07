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

    @GetMapping("/unread/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@PathVariable int userId) {
        List<NotificationResponse> responses = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable int userId) {
       List<NotificationResponse>  response = notificationService.findByUserId(userId);
       return ResponseEntity.ok(response);
    }
    @PutMapping("/read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable int id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable int id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

}
