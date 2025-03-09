package com.blogwaves.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogwaves.main.payloads.ApiResponse;
import com.blogwaves.main.payloads.CategoryDto;
import com.blogwaves.main.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// POST - create new category
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

		CategoryDto createdCategoryDto = categoryService.createCategory(categoryDto);

		return new ResponseEntity<>(createdCategoryDto, HttpStatus.CREATED);
	}

	// GET - get all categories

	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories() {

		return ResponseEntity.ok(categoryService.getAllCategories());
	}

	// GET - get a category by id

	@GetMapping("/{cId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable int cId) {

		return ResponseEntity.ok(categoryService.getCategoryById(cId));
	}

	// PUT - update a category

	@PutMapping("/{cId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable int cId) {
		CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, cId);

		return ResponseEntity.ok(updatedCategory);
	}

	// DELETE - delete a user
	//Only admin can delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{cId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int cId) {

		categoryService.deleteCategoryById(cId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted successfully", true), HttpStatus.OK);
	}
}
