package com.example.backend.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrokenRequest {
    int equipmentItemId;
    int usersId;
    LocalDateTime brokenDate;
    String description;
}
