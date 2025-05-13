package com.example.backend.dto.response;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponse {
    int id;
    String usersFirstName;
    String usersLastName;
    String message;
    boolean isRead;

}
