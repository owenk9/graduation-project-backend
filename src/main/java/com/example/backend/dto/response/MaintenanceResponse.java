package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class MaintenanceResponse {
    int id;
    String equipmentName;
    LocalDateTime maintenanceDate;
    String description;
    String status;
    double cost;
    String technician;
}
