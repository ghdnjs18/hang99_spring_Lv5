package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.CommentRequestDto;
import com.sparta.springboottest.dto.CommentResponseDto;
import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.Comment;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.jwt.JwtUtil;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.CommentRepository;
import com.sparta.springboottest.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CommentResponseDto createComment(CommentRequestDto requestDto, String tokenValue) {
        Long boardId = requestDto.getBoardId();
        Board board = findBoard(boardId);

        String username = tokenUsername(tokenValue);
        User user = findUser(username);

        Comment comment = new Comment(requestDto, board, user);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new NullPointerException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NullPointerException("해당 유저는 존재하지 않습니다.")
        );
    }

    private String tokenUsername(String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        Claims info = jwtUtil.getUserInfoFromToken(token);

        return info.getSubject();
    }
}
