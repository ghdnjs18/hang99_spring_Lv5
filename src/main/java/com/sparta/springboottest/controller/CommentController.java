package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.CommentRequestDto;
import com.sparta.springboottest.dto.CommentResponseDto;
import com.sparta.springboottest.jwt.JwtUtil;
import com.sparta.springboottest.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        return commentService.createComment(requestDto, tokenValue);
    }
}
