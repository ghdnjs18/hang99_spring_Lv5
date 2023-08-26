package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
