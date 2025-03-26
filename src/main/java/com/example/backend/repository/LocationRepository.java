package com.example.backend.repository;

import com.example.backend.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    boolean existsByLocationName(String name);
}
