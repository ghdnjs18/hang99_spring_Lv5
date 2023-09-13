package com.sparta.springboottest.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {
    private String title;
    private String contents;
    private Long categoryId;
}
