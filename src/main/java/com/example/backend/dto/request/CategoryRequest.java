package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotNull(message = "Category name cannot be null")
    String categoryName;
    String description;
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
