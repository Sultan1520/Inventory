package com.university.inventory.mapper;

import com.university.inventory.dto.category.CategoryResponse;
import com.university.inventory.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category c) {
        if (c == null) return null;
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription());
    }
}
