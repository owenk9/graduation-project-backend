package com.example.backend.dto.request;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaintenanceRequest {
    int equipmentId;
    LocalDateTime maintenanceDate;
    String description;
    String status;
    double cost;
    String technician;
}
