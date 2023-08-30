package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.dto.ItemResponseDto;
import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.jwt.JwtUtil;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public BoardResponseDto createBoard(BoardRequestDto requestDto, String tokenValue) {
        String username = tokenUsername(tokenValue);
        User user = findUser(username);

        Board board = new Board(requestDto);
        board.setUser(user);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getBoards() {
        ItemResponseDto responseDto = new ItemResponseDto();
        for (BoardResponseDto board : boardRepository.findAllByOrderByModifiedTimeDesc().stream().map(BoardResponseDto::new).toList()) {
            responseDto.setBoard(board);
        }
        return responseDto;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);

        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, String tokenValue) {
        Board board = findBoard(id);
        String username = board.getUser().getUsername();

        if (username.equals(tokenUsername(tokenValue))) {
            board.update(requestDto);
        }

        return new BoardResponseDto(board);
    }

    public ResponseEntity<Map> deleteBoard(Long id, String tokenValue) {
        Board board = findBoard(id);
        String username = board.getUser().getUsername();
        System.out.println(username);

        if (username.equals(tokenUsername(tokenValue))) {
            boardRepository.delete(board);

            return ResponseEntity.status(HttpStatus.OK).body(makeJson("게시물 삭제를 성공했습니다."));
        }

        return null;
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 유저는 존재하지 않습니다.")
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

    private Map<String, String> makeJson(String message) {
        Map<String, String> map = new HashMap();
        map.put("msg", message);
        map.put("statusCode", String.valueOf(HttpStatus.OK).substring(0, 3));

        return map;
    }
}
