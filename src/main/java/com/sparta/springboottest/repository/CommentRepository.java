package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndCommentUseTrue(Long id);
}
