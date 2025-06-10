package com.example.backend.repository;

import com.example.backend.entity.Category;
import com.example.backend.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String name);
    Page<Category> findByCategoryNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Category> findByCategoryName(String name);

    Optional<Category> findFirstByCategoryName(String categoryName);
}
