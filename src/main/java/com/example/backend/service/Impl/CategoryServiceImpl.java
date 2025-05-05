package com.example.backend.service.Impl;

import com.example.backend.dto.request.CategoryRequest;
import com.example.backend.dto.response.CategoryResponse;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entity.Category;
import com.example.backend.entity.Equipment;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.CategoryMapper;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        boolean categoryExists = categoryRepository.existsByCategoryName(categoryRequest.getCategoryName());
        if(categoryExists){
            throw new DuplicateResourceException("Category " + categoryRequest.getCategoryName() +  " already exists");
        }
        Category category = categoryMapper.toCategory(categoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot found category with id " + id));
        existingCategory.setCategoryName(categoryRequest.getCategoryName());
        existingCategory.setDescription(categoryRequest.getDescription());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot found category with id " + id));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategory(Pageable pageable) {
        Page<Category> getAll = categoryRepository.findAll(pageable);
        return getAll.map(categoryMapper::toCategoryResponse);
    }

    @Override
    public Page<CategoryResponse> findCategoryByName(String name, Pageable pageable) {
        Page<Category> category = categoryRepository.findByCategoryNameContainingIgnoreCase(name, pageable);
        return category.map(categoryMapper::toCategoryResponse);
    }
}
