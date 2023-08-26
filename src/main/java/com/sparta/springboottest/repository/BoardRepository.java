package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedTimeDesc();
    List<Board> findAllByTitleContainingOrderByModifiedTimeDesc(String title);
    List<Board> findAllByUsernameContainingOrderByModifiedTimeDesc(String username);
    List<Board> findAllByContentsContainingOrderByModifiedTimeDesc(String contents);
    List<Board> findAllByModifiedTimeBeforeOrderByModifiedTimeDesc(LocalDateTime modifiedTime);

}
