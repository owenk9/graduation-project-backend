package com.example.backend.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EquipmentItemResponse {
    int id;
    int equipmentId;
    String equipmentName;
    String serialNumber;
    String status;
    LocalDateTime purchaseDate;
    String locationName;
}
