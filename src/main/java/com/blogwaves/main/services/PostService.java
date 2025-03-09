package com.blogwaves.main.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.blogwaves.main.payloads.PostDto;
import com.blogwaves.main.payloads.PostResponse;

import jakarta.validation.Valid;

public interface PostService {

	// Create new Post
	public PostDto createPost(@Valid PostDto postDto, int uId, int cId, String path, MultipartFile mFile) throws IOException;

	// For uploading image of post when creating a new post
	public String uploadPostImage(String path, MultipartFile mFile) throws IOException;
	
	//For showing or downloading a image of a post
	public InputStream getImage(String path, String imageName) throws FileNotFoundException;

	// Get Single Post
	public PostDto getPostById(int pId);

	// Get all Posts
	public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir);

	// Update a post
	public PostDto updatePost(@Valid PostDto postDto, int pId, String path, MultipartFile mFile) throws IOException;

	// Delete a post
	public void deletePost(int pId);

	// Get all posts by category
	// public List<PostDto> getAllPostsByCategory(int cId);
	public PostResponse getAllPostsByCategory(int cId, int pageNumber, int pageSize, String sortBy, String sortDir);

	// Get all posts by user
	// public List<PostDto> getAllPostsByUser(int uId);
	public PostResponse getAllPostsByUser(int uId, int pageNumber, int pageSize, String sortBy, String sortDir);

	// Search Posts
//	public List<PostDto> searchPost(String keyword);
	public PostResponse searchPost(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);
}
