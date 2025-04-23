package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationRequest {
    @NotNull(message = "Location name cannot be null")
    String locationName;

}
