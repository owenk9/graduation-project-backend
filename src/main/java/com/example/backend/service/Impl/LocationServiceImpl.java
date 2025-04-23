package com.example.backend.service.Impl;

import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.entity.Location;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.LocationMapper;
import com.example.backend.repository.LocationRepository;
import com.example.backend.service.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    LocationMapper locationMapper;
    LocationRepository locationRepository;
    @Override
    public LocationResponse addLocation(LocationRequest locationRequest) {
        boolean locationExists = locationRepository.existsByLocationName(locationRequest.getLocationName());
        if (locationExists) {
            throw new DuplicateResourceException("Location " + locationRequest.getLocationName() + " already exists");
        }
        Location location = locationMapper.toLocation(locationRequest);
        return locationMapper.toLocationResponse(locationRepository.save(location));
    }

    @Override
    public LocationResponse updateLocation(int id, LocationRequest locationRequest) {
        Location existingLocation = locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot found location with id " + id));
        existingLocation.setLocationName(locationRequest.getLocationName());
        Location updatedLocation = locationRepository.save(existingLocation);
        return locationMapper.toLocationResponse(updatedLocation);
    }

    @Override
    public void deleteLocation(int id) {
        locationRepository.deleteById(id);
    }

    @Override
    public LocationResponse getLocationById(int id) {
        Location location = locationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot found location with id " + id));
        return locationMapper.toLocationResponse(location);
    }

    @Override
    public Page<LocationResponse> getAllLocation(Pageable pageable) {
        Page<Location> getAll = locationRepository.findAll(pageable);
        return getAll.map(locationMapper::toLocationResponse);
    }

    @Override
    public Page<LocationResponse> findLocationByName(String name, Pageable pageable) {
        Page<Location> locations = locationRepository.findByLocationNameContainingIgnoreCase(name, pageable);
        return locations.map(locationMapper::toLocationResponse);
    }
}
