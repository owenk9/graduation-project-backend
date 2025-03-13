package com.example.backend.mapper;

import com.example.backend.dto.request.CategoryRequest;
import com.example.backend.dto.request.LocationRequest;
import com.example.backend.dto.response.CategoryResponse;
import com.example.backend.dto.response.LocationResponse;
import com.example.backend.entity.Category;
import com.example.backend.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);
    CategoryResponse toCategoryResponse(Category category);
}
