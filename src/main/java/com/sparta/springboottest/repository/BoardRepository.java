package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findAllByOrderByModifiedTimeDesc();
}
