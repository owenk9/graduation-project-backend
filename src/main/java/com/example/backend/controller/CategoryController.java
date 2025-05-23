package com.example.backend.controller;

import com.example.backend.dto.request.CategoryRequest;
import com.example.backend.dto.response.CategoryResponse;
import com.example.backend.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryController {
    CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest){
        CategoryResponse addCategory = categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(201).body(addCategory);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable int id, @RequestBody CategoryRequest categoryRequest){
        CategoryResponse updateCategory = categoryService.updateCategory(id,categoryRequest);
        return ResponseEntity.ok(updateCategory);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<CategoryResponse>> getCategory(@RequestParam(required = false) String name,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int pageSize){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<CategoryResponse> categoryPage;
        if(name != null){
            categoryPage = categoryService.findCategoryByName(name,pageable);
        } else {
            categoryPage = categoryService.getAllCategory(pageable);
        }
        return ResponseEntity.ok(categoryPage);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable int id){
        CategoryResponse getCategoryById = categoryService.getCategoryById(id);
        return ResponseEntity.ok(getCategoryById);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable int id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
