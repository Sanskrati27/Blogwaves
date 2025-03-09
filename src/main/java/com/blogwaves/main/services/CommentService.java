package com.blogwaves.main.services;

import com.blogwaves.main.payloads.CommentDto;

public interface CommentService {

	// Create new Comment
	public CommentDto createComment(CommentDto commentDto, int postId, int userId);

	// Update a Comment
	public CommentDto updateComment(CommentDto commentDto, int commentId);

	// Delete a Comment
	public void deleteCommentById(int commentId);

}
