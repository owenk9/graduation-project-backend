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
    Integer categoryId;
    String description;
}
