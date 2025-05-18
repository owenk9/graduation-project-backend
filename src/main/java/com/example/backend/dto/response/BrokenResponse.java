package com.example.backend.dto.response;

import com.example.backend.enums.BrokenStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrokenResponse {
    int id;
    String equipmentName;
    String serialNumber;
    BrokenStatus status;
    String fullName;
    LocalDateTime brokenDate;
    String description;
}
