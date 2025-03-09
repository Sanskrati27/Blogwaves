package com.blogwaves.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogwaves.main.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
