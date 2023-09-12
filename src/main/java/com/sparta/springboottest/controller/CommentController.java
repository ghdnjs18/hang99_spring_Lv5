package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.CommentRequestDto;
import com.sparta.springboottest.dto.CommentResponseDto;
import com.sparta.springboottest.dto.MessageResponseDto;
import com.sparta.springboottest.security.UserDetailsImpl;
import com.sparta.springboottest.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(id, requestDto, userDetails.getUser());
    }

    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    @PutMapping("/comment/like/{id}")
    public ResponseEntity<MessageResponseDto> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(id, userDetails.getUser());
    }

    @PostMapping("/comment/comment/{id}")
    public CommentResponseDto createCommentComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createCommentComment(id, requestDto, userDetails.getUser());
    }
}
