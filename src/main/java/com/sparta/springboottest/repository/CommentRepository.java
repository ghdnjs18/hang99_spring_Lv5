package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoard_idOrderByModifiedTimeDesc(Long board_id);
}
