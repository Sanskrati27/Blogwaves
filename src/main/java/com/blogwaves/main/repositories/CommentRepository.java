package com.blogwaves.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogwaves.main.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
