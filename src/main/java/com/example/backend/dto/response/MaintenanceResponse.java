package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class MaintenanceResponse {
    int id;
    String equipmentName;
    int equipmentItemId;
    String serialNumber;
    LocalDateTime maintenanceDate;
    String description;
    String status;
    String locationName;
    double cost;
    String technician;
}
