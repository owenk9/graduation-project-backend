package com.example.backend.service;

import com.example.backend.dto.request.CategoryRequest;
import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.response.CategoryResponse;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationService {
    LocationResponse addLocation(LocationRequest locationRequest);
    LocationResponse updateLocation(int id, LocationRequest locationRequest);
    void deleteLocation(int id);
    LocationResponse getLocationById(int id);
    Page<LocationResponse> getAllLocation(Pageable pageable);
    Page<LocationResponse> findLocationByName(String name, Pageable pageable);
}
