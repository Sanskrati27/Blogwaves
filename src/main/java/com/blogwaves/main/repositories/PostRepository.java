package com.blogwaves.main.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogwaves.main.entities.Category;
import com.blogwaves.main.entities.Post;
import com.blogwaves.main.entities.User;

public interface PostRepository extends JpaRepository<Post, Integer> {

	Page<Post> findByUser(User user, Pageable p);
	
	Page<Post> findByCategory(Category category, Pageable p);
	
	Page<Post> findByTitleContaining(String title, Pageable p);

	
	//	List<Post> findByTitleContaining(String title);
	
	//List<Post> findByUser(User user);
	
	//List<Post> findByCategory(Category category);
	
}
