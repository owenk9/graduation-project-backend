package com.example.backend.dto.request;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaintenanceRequest {
    int equipmentItemId;
    String serialNumber;
    LocalDateTime maintenanceDate;
    String description;
    String status;
    double cost;
    String technician;
}
