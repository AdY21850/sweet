package com.example.sweet.service;

import com.example.sweet.exception.ResourceNotFoundException;
import com.example.sweet.model.Category;
import com.example.sweet.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ✅ Get all categories (PUBLIC)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ Add category (ADMIN)
    public Category addCategory(Category category) {

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }

        return categoryRepository.save(category);
    }

    // ✅ Get category by ID
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id " + id)
                );
    }

    // ✅ Update category (ADMIN)
    public Category updateCategory(Long id, Category updatedCategory) {

        Category category = getById(id);

        category.setName(updatedCategory.getName());
        category.setImageUrl(updatedCategory.getImageUrl());
        category.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(category);
    }

    // ✅ Delete category (ADMIN)
    public void deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id " + id);
        }

        categoryRepository.deleteById(id);
    }
}
