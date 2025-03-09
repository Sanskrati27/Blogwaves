package com.blogwaves.main.services.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogwaves.main.entities.Comment;
import com.blogwaves.main.entities.Post;
import com.blogwaves.main.entities.User;
import com.blogwaves.main.exceptions.ResourceNotFoundException;
import com.blogwaves.main.payloads.CommentDto;
import com.blogwaves.main.repositories.CommentRepository;
import com.blogwaves.main.repositories.PostRepository;
import com.blogwaves.main.repositories.UserRepository;
import com.blogwaves.main.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, int postId, int userId) {
		
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		Comment comment = modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setCommentDate(new Date());
		comment.setUser(user);
		
		Comment createdComment = commentRepository.save(comment);
		
		return modelMapper.map(createdComment, CommentDto.class);
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, int commentId) {

		Comment comment = commentRepository.findById(commentId)
		.orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));

		comment.setContent(commentDto.getContent());
		comment.setCommentDate(new Date());
		
		Comment updatedComment = commentRepository.save(comment);

		return modelMapper.map(updatedComment, CommentDto.class);
	}

	@Override
	public void deleteCommentById(int commentId) {
		
		commentRepository.findById(commentId)
		.orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
		
		commentRepository.deleteById(commentId);

	}

}
