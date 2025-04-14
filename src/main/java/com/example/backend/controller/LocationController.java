package com.example.backend.controller;

import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.service.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LocationController {
    LocationService locationService;
    @PostMapping("/add")
    public ResponseEntity<LocationResponse> addLocation(@RequestBody LocationRequest locationRequest){
        LocationResponse addLocation = locationService.addLocation(locationRequest);
        return ResponseEntity.status(201).body(addLocation);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable int id, @RequestBody LocationRequest locationRequest){
        LocationResponse updateLocation = locationService.updateLocation(id, locationRequest);
        return ResponseEntity.ok(updateLocation);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<LocationResponse>> getAllLocation(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int pageSize){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<LocationResponse> getAllLocation = locationService.getAllLocation(pageable);
        return ResponseEntity.ok(getAllLocation);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable int id){
        LocationResponse getLocationById = locationService.getLocationById(id);
        return ResponseEntity.ok(getLocationById);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLocationById(@PathVariable int id){
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
