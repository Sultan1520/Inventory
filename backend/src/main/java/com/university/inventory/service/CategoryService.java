package com.university.inventory.service;

import com.university.inventory.dto.category.CategoryRequest;
import com.university.inventory.dto.category.CategoryResponse;
import com.university.inventory.entity.Category;
import com.university.inventory.exception.ConflictException;
import com.university.inventory.exception.NotFoundException;
import com.university.inventory.mapper.CategoryMapper;
import com.university.inventory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest req) {
        if (categoryRepository.existsByNameIgnoreCase(req.name())) {
            throw new ConflictException("Category already exists: " + req.name());
        }
        Category c = Category.builder()
                .name(req.name())
                .description(req.description())
                .build();
        return categoryMapper.toResponse(categoryRepository.save(c));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req) {
        Category c = get(id);
        c.setName(req.name());
        c.setDescription(req.description());
        return categoryMapper.toResponse(c);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(get(id));
    }

    private Category get(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Category", id));
    }
}
