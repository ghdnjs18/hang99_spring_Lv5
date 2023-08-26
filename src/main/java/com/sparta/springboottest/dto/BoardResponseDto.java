package com.sparta.springboottest.dto;

import com.sparta.springboottest.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private String password;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.password = board.getPassword();
        this.createdTime = board.getCreatedTime();
        this.modifiedTime = board.getModifiedTime();
    }
}
