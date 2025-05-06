package com.example.backend.mapper;

import com.example.backend.dto.request.NotificationRequest;
import com.example.backend.dto.response.NotificationResponse;
import com.example.backend.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest notificationRequest);
    @Mapping(source = "users.firstName", target = "usersFirstName")
    @Mapping(source = "users.lastName", target = "usersLastName")
    NotificationResponse toNotificationResponse(Notification notification);
}
