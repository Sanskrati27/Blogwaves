package com.blogwaves.main.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogwaves.main.payloads.ApiResponse;
import com.blogwaves.main.payloads.CommentDto;
import com.blogwaves.main.services.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;

	// POST - create new comment by postId
	@PostMapping("post/{postId}/comments/{userId}")
	public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentDto commentDto, 
			@PathVariable int postId,  
			@PathVariable int userId) {

		CommentDto createdComment = commentService.createComment(commentDto, postId, userId);

		return new ResponseEntity<CommentDto>(createdComment, HttpStatus.CREATED);
	}

	// PUT - update a comment

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,
			@PathVariable int commentId) {
		CommentDto updatedComment = commentService.updateComment(commentDto, commentId);

		return ResponseEntity.ok(updatedComment);
	}

	// DELETE - delete a comment by comment id
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable int commentId) {

		commentService.deleteCommentById(commentId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
	}
}
