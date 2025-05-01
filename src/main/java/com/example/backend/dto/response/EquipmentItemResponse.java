package com.example.backend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentItemResponse {
    int id;
    int equipmentId;
    String serialNumber;
    String status;
    String locationName;
    LocalDateTime returnDate;
}
