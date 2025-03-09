package com.blogwaves.main.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogwaves.main.config.ConstantValues;
import com.blogwaves.main.payloads.ApiResponse;
import com.blogwaves.main.payloads.PostDto;
import com.blogwaves.main.payloads.PostResponse;
import com.blogwaves.main.services.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${project.image}")
	private String imagePath;
	
	private Logger logger = LoggerFactory.getLogger(PostController.class);

	// POST - create new post
	@PostMapping(value = "/user/{userId}/category/{categoryId}/posts/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createPost(
	        @PathVariable int userId,
	        @PathVariable int categoryId,
	        @RequestParam(value = "postDto") String postDtoJson,
	        @RequestPart(value = "image", required = false) MultipartFile imageName) throws IOException {

		logger.info("Create new post request received. User ID: {}, Category ID: {}", userId, categoryId);
	    logger.info("Incoming post data: {}", postDtoJson);
	    logger.info("Authorization: {}", SecurityContextHolder.getContext().getAuthentication());
		
		PostDto postDto = null;
		
		//Converting string to Json
//		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			postDto = objectMapper.readValue(postDtoJson, PostDto.class);
		}
		catch (JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Data");
		}
		
		//  Log image details
	    if (imageName != null) {
	        logger.info("Image file received: {}", imageName.getOriginalFilename());
	    } else {
	        logger.warn("No image file uploaded.");
	    }
	    
		PostDto createdPostDto = postService.createPost(postDto, userId, categoryId, imagePath, imageName);
		
		logger.info("Post : {}", postDtoJson );

		return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
	}
	
	// GET - get all posts by a user
	@GetMapping("/user/{userId}/posts/")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable int userId,
			@RequestParam(value = "pageNumber", defaultValue = ConstantValues.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ConstantValues.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = ConstantValues.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ConstantValues.SORT_DIR, required = false) String sortDir) {

		return ResponseEntity.ok(postService.getAllPostsByUser(userId, pageNumber, pageSize, sortBy, sortDir));
	}

	// GET - get all posts by a category
	@GetMapping("/category/{categoryId}/posts/")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable int categoryId,
			@RequestParam(value = "pageNumber", defaultValue = ConstantValues.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ConstantValues.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = ConstantValues.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ConstantValues.SORT_DIR, required = false) String sortDir) {

		return ResponseEntity.ok(postService.getAllPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir));
	}

	// GET - get all posts
	@GetMapping("/posts/")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = ConstantValues.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ConstantValues.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = ConstantValues.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ConstantValues.SORT_DIR, required = false) String sortDir) {

		 logger.info("Incoming post data: {}", pageNumber, pageSize, sortBy, sortDir);
		 logger.info("Authorization: {}", SecurityContextHolder.getContext().getAuthentication());
		
		 return ResponseEntity.ok(postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir));
	}

	// GET - get a post by Id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable int postId) {

		return ResponseEntity.ok(postService.getPostById(postId));
	}

	// DELETE - delete a post by post id
	@DeleteMapping("/posts/{pId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int pId) {

		postService.deletePost(pId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully", true), HttpStatus.OK);
	}

	// PUT - update a post
	@PutMapping("/posts/{pId}")
	public ResponseEntity<?> updatePost(
//			@Valid @RequestBody PostDto postDto, 
			@PathVariable int pId,
			@RequestParam(value = "postDto", required = false) String postDtoJson,
	        @RequestParam(value = "image", required = false) MultipartFile imageName) throws IOException {

		PostDto postDto = null;
		
		if(postDtoJson != null) {
			//Converting string to Json
			try {
				postDto = objectMapper.readValue(postDtoJson, PostDto.class);
			}
			catch (JsonProcessingException e) {
			
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Data");
			}
		}
		
		PostDto updatedPost = postService.updatePost(postDto, pId, imagePath, imageName);

		return ResponseEntity.ok(updatedPost);
	}

	// GET - search a post by title
	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<PostResponse> searchPostByTitle(@PathVariable String keyword,
			@RequestParam(value = "pageNumber", defaultValue = ConstantValues.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = ConstantValues.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = ConstantValues.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = ConstantValues.SORT_DIR, required = false) String sortDir) {

//		List<PostDto> searchPosts = postService.searchPost(keyword, pageNumber, pageSize, sortBy, sortDir);
		
		return ResponseEntity.ok(postService.searchPost(keyword, pageNumber, pageSize, sortBy, sortDir));
	}
	
	//GET - method to serve or download image files
	@GetMapping(value = "posts/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName,
			HttpServletResponse response) throws IOException {
		
		InputStream resource = postService.getImage(imagePath, imageName);
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	
	
	// GET - get all posts by a user
//		@GetMapping("/user/{userId}/posts/")
//		public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable int userId) {
//
//			return ResponseEntity.ok(postService.getAllPostsByUser(userId));
//		}
}
