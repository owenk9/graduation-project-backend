package com.example.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentItemRequest {
    int equipmentId;
    @NotBlank(message = "Serial number not null")
    String serialNumber;
    String status;
    LocalDateTime purchaseDate;
    Integer locationId;
}
