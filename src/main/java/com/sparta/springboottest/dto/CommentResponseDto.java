package com.sparta.springboottest.dto;

import com.sparta.springboottest.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String username;
    private int like;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.username = comment.getUsername();
        this.like = comment.getLike();
        this.createdTime = comment.getCreatedTime();
        this.modifiedTime = comment.getModifiedTime();
    }
}
