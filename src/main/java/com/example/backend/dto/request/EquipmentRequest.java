package com.example.backend.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class EquipmentRequest {
    String name;
    int quantity;
    Integer categoryId;
    String status;
    Integer locationId;
    LocalDateTime purchaseDate;
    String description;
}
