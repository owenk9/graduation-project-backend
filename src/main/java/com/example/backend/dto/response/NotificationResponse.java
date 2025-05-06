package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {
    int id;
    String usersFirstName;
    String usersLastName;
    String message;
    boolean isRead;
}
