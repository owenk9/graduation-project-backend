package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class StatusDistribution {
    private String status;
    private Long count;
    public StatusDistribution(String status, Long count) {
        this.status = status;
        this.count = count;
    }
}
