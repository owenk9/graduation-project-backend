package com.example.backend.service;

import com.example.backend.dto.request.CategoryRequest;
import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.response.CategoryResponse;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryService{
    CategoryResponse addCategory(CategoryRequest categoryRequest);
    CategoryResponse updateCategory(int id, CategoryRequest categoryRequest);
    void deleteCategory(int id);
    CategoryResponse getCategoryById(int id);
    Page<CategoryResponse> getAllCategory(Pageable pageable);
    Page<CategoryResponse> findCategoryByName(String name, Pageable pageable);
}
