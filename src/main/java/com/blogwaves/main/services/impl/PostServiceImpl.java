package com.blogwaves.main.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.blogwaves.main.entities.Category;
import com.blogwaves.main.entities.Post;
import com.blogwaves.main.entities.User;
import com.blogwaves.main.exceptions.PostNotFoundException;
import com.blogwaves.main.exceptions.ResourceNotFoundException;
import com.blogwaves.main.payloads.PostDto;
import com.blogwaves.main.payloads.PostResponse;
import com.blogwaves.main.repositories.CategoryRepository;
import com.blogwaves.main.repositories.PostRepository;
import com.blogwaves.main.repositories.UserRepository;
import com.blogwaves.main.services.PostService;

@Service
@Validated
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public PostDto createPost(PostDto postDto, int uId, int cId, String path, MultipartFile image) throws IOException {

		User user = userRepository.findById(uId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", uId));

		Category category = categoryRepository.findById(cId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", cId));

		Post post = modelMapper.map(postDto, Post.class);

		// ✅ Handle null image properly
	    if (image != null) {
	        post.setImageName(uploadPostImage(path, image));
	    } else {
//	    	throw new NullPointerException("File is empty. Please upload a valid image.");
	        post.setImageName("default.jpg"); // Use default image if no file is uploaded
	    }
	    
		post.setPostDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post createdPost = postRepository.save(post);

		return modelMapper.map(createdPost, PostDto.class);

	}

	@Override
	public PostDto getPostById(int pId) {

		Post post = postRepository.findById(pId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", pId));

		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir) {

//		Sort sort = null;
//		if(sortDir.equalsIgnoreCase("asc")) {
//			sort = Sort.by(sortBy).ascending();
//		}
//		else {
//			sort = Sort.by(sortBy).descending();
//		}
		
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		//Sort data in descending order
		//Pageable p = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		
		
		Page<Post> pagePost = postRepository.findAll(p);

		List<Post> posts = pagePost.getContent();
		
//		System.out.println(posts);

		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostDto updatePost(PostDto postDto, int pId, String path, MultipartFile image) throws IOException {

		Post post = postRepository.findById(pId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", pId));

		if(postDto != null) {
			post.setTitle(postDto.getTitle());
			post.setContent(postDto.getContent());
		}
		
		if (image != null) {
	        post.setImageName(uploadPostImage(path, image));
	    }
		
		post.setPostDate(new Date());
		
		Post updatedPost = postRepository.save(post);

		return modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(int pId) {
		postRepository.findById(pId).orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", pId));

		postRepository.deleteById(pId);
	}

	@Override
	public PostResponse getAllPostsByCategory(int cId, int pageNumber, int pageSize, String sortBy, String sortDir) {

		Category category = categoryRepository.findById(cId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", cId));

		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = postRepository.findByCategory(category, p);

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostResponse getAllPostsByUser(int uId, int pageNumber, int pageSize, String sortBy, String sortDir) {

		User user = userRepository.findById(uId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", uId));

		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = postRepository.findByUser(user, p);

		List<Post> posts = pagePost.getContent();

		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostResponse searchPost(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending() ;
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> pagePost = postRepository.findByTitleContaining(keyword, p);

		List<Post> posts = pagePost.getContent();
		
		// Check if post list is empty
		if(posts.isEmpty()) {
			throw new PostNotFoundException("Post", "title", keyword);
		}
			
		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalRecords(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public String uploadPostImage(String path, MultipartFile image) throws IOException {

	    // Check for valid MIME type
	    String contentType = image.getContentType();
	    if (contentType == null || (!contentType.equals("image/jpeg") && 
	                                !contentType.equals("image/png") && 
	                                !contentType.equals("image/jpg"))) {
	        throw new IllegalArgumentException("Invalid file type! Only JPG, JPEG, and PNG images are allowed.");
	    }
	    
		//Original File Name
		String imageOriginalName = image.getOriginalFilename();
		
		//Randomly generated file name
		String randomId = UUID.randomUUID().toString();
		//Substring will extract the extension of image name
		String imageNewName = randomId.concat(imageOriginalName.substring(imageOriginalName.lastIndexOf(".")));
		
		//Full path where image get store
		String imagePath = path + File.separator + imageNewName;
		
		//Create folder if folder does not exist
		File folder = new File(path);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		//It will store the image in respective folder
		Files.copy(image.getInputStream(), Paths.get(imagePath));
		return imageNewName;
	}

	@Override
	public InputStream getImage(String path, String imageName) throws FileNotFoundException {
		
		//Full path where image get store
		String imagePath = path + File.separator + imageName; 
		InputStream is = new FileInputStream(imagePath);
		
		//database logic to return input stream
		return is;
	}

//	@Override
//	public List<PostDto> getAllPostsByCategory(int cId) {
//
//		Category category = categoryRepository.findById(cId)
//	.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", cId));

//		List<Post> posts = postRepository.findByCategory(category);
//
//		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//
//		return postDtos;
//	}

//	@Override
//	public List<PostDto> getAllPostsByUser(int uId) {
//
//		User user = userRepository.findById(uId)
//				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", uId));
//
//		List<Post> posts = postRepository.findByUser(user);
//
//		List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//
//		return postDtos;
//	}

//	@Override
//	public PostDto updatePost(PostDto postDto, int pId) {
//
//		Post post = postRepository.findById(pId)
//				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", pId));
//
//		post.setTitle(postDto.getTitle());
//		post.setContent(postDto.getContent());
//		post.setImageName(postDto.getImageName());
//
//		Post updatedPost = postRepository.save(post);
//
//		return modelMapper.map(updatedPost, PostDto.class);
//	}
}
