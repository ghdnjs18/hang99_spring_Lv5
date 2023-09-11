package com.sparta.springboottest.repository;

import com.sparta.springboottest.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    CommentLike findByUser_IdAndComment_Id(Long userId, Long commentId);
}
