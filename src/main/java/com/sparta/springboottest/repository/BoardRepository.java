package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByCreatedTimeDesc();
}
