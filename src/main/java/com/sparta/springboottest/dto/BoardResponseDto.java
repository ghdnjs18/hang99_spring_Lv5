package com.sparta.springboottest.dto;

import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUser().getUsername();
        this.contents = board.getContents();
        this.createdTime = board.getCreatedTime();
        this.modifiedTime = board.getModifiedTime();
    }

    public void setComment(CommentResponseDto comment) {
        commentList.add(comment);
    }
}
