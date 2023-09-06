package com.sparta.springboottest.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long boardId;
    private String comment;
}
