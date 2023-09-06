package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
