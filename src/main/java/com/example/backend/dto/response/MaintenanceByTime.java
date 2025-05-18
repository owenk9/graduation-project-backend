package com.example.backend.dto.response;

import lombok.Data;

@Data
public class MaintenanceByTime {
    private String period;
    private Long maintenanceCount;
    private Double totalCost;

    public MaintenanceByTime(String period, Long maintenanceCount, Double totalCost) {
        this.period = period;
        this.maintenanceCount = maintenanceCount;
        this.totalCost = totalCost;
    }
}