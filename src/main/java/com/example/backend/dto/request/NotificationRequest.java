package com.example.backend.dto.request;

import lombok.Data;

@Data
public class NotificationRequest {
    int usersId;
    String message;
}
