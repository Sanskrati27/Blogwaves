package com.blogwaves.main.services;

import java.util.List;

import com.blogwaves.main.payloads.CategoryDto;

public interface CategoryService {

	// Create new Category
	public CategoryDto createCategory(CategoryDto categoryDto);

	// Get Single Category
	public CategoryDto getCategoryById(int cId);

	// Get all Categories
	public List<CategoryDto> getAllCategories();

	// Update a Category
	public CategoryDto updateCategory(CategoryDto categoryDto, int cId);

	// Delete a Category
	public void deleteCategoryById(int cId);
}
