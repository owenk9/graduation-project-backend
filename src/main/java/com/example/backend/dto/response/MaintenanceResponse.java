package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class MaintenanceResponse {
    int id;
    int equipmentId;
    LocalDateTime maintenanceDate;
    String description;
    double cost;
    String technician;
}
