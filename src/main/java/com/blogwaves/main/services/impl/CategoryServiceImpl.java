package com.blogwaves.main.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogwaves.main.entities.Category;
import com.blogwaves.main.exceptions.ResourceNotFoundException;
import com.blogwaves.main.payloads.CategoryDto;
import com.blogwaves.main.repositories.CategoryRepository;
import com.blogwaves.main.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		Category category = modelMapper.map(categoryDto, Category.class);
		Category createdCat = categoryRepository.save(category);
		
		return modelMapper.map(createdCat, CategoryDto.class);
		
	}

	@Override
	public CategoryDto getCategoryById(int cId) {
		
		Category category = categoryRepository.findById(cId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cId));

		return modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {

		List<Category> categories = categoryRepository.findAll();

		List<CategoryDto> categoryDtos = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());

		return categoryDtos;
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, int cId) {
		Category category = categoryRepository.findById(cId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cId));

		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		
		Category updatedCategory = categoryRepository.save(category);

		return modelMapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategoryById(int cId) {
		categoryRepository.findById(cId)
		.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cId));
		
		categoryRepository.deleteById(cId);

	}

}
