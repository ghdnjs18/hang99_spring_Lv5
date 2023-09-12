package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByBoardUseTrue(Pageable pageable);
    Optional<Board> findByIdAndBoardUseTrue(Long id);
}
