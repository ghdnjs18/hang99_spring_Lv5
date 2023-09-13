package com.sparta.springboottest.dto;

import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.Category;
import com.sparta.springboottest.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private int boardLike;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private List<Comment> commentList;
    private Category category;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.boardLike = board.getBoardLike();
        this.createdTime = board.getCreatedTime();
        this.modifiedTime = board.getModifiedTime();
        this.commentList = board.getCommentList();
        this.category = board.getCategory();
    }
}
