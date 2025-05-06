package com.example.backend.service;

import com.example.backend.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse sendNotification(int userId, String message);
    List<NotificationResponse> getUnreadNotifications(int userId);
}
