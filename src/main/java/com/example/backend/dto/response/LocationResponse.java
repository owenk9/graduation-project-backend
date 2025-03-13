package com.example.backend.dto.response;

import com.example.backend.entity.Equipment;
import lombok.Data;

import java.util.List;

@Data
public class LocationResponse {
    int id;
    String locationName;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
