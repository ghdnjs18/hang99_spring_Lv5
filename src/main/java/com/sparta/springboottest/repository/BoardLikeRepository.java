package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    BoardLike findByUser_IdAndBoard_Id(Long id, Long id1);
}
