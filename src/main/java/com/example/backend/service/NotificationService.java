package com.example.backend.service;

import com.example.backend.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse sendNotification(int userId, String message);
    List<NotificationResponse> getUnreadNotifications(int userId);
    void markAsRead(int notificationId);
    void deleteNotification(int notificationId);
    List<NotificationResponse> findByUserId(int userId);
}
