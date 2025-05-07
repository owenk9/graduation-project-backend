package com.example.backend.service.Impl;

import com.example.backend.dto.request.NotificationRequest;
import com.example.backend.dto.response.NotificationResponse;
import com.example.backend.entity.Notification;
import com.example.backend.entity.Users;
import com.example.backend.mapper.NotificationMapper;
import com.example.backend.repository.NotificationRepository;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    NotificationRepository notificationRepository;
    UsersRepository usersRepository;
    NotificationMapper notificationMapper;
    @Override
    public NotificationResponse sendNotification(int userId, String message) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        NotificationRequest request = new NotificationRequest();
        request.setMessage(message);
        request.setUsersId(user.getId());
        Notification notification = notificationMapper.toNotification(request);
        notification.setUsers(user);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(savedNotification);
    }
    @Transactional
    @Override
    public List<NotificationResponse> getUnreadNotifications(int userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        return notifications.stream().map(notificationMapper::toNotificationResponse).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(int notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
        notificationRepository.markAsRead(notificationId);
    }

    @Override
    public void deleteNotification(int notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public List<NotificationResponse> findByUserId(int userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().map(notificationMapper::toNotificationResponse).collect(Collectors.toList());
    }
}
