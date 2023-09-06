package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.*;
import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.entity.UserRoleEnum;
import com.sparta.springboottest.jwt.JwtUtil;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.CommentRepository;
import com.sparta.springboottest.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    public BoardResponseDto createBoard(BoardRequestDto requestDto, String tokenValue) {
        String username = tokenUsername(tokenValue);
        Board board = new Board(requestDto);

        User user = findUser(username);
        user.addBoardList(board);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getBoards() {
        ItemResponseDto responseDto = new ItemResponseDto();
        List<BoardResponseDto> list = boardRepository.findAllByOrderByModifiedTimeDesc().stream().map(BoardResponseDto::new).toList();
        for(BoardResponseDto boardResponseDto : list){
            responseDto.setBoard(boardResponseDto);
        }
        return responseDto;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        BoardResponseDto responseDto = new BoardResponseDto(board);

        return responseDto;
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, String tokenValue) {
        Board board = findBoard(id);
        String username = board.getUser().getUsername();

        if (!username.equals(tokenUsername(tokenValue))) {
            if (findUser(tokenUsername(tokenValue)).getRole() == UserRoleEnum.ADMIN) {
                board.update(requestDto);

                return new BoardResponseDto(board);
            }
            throw new IllegalArgumentException("해당 게시물의 작성자만 수정할 수 있습니다.");
        }
        board.update(requestDto);

        return new BoardResponseDto(board);
    }

    public ResponseEntity<MessageResponseDto> deleteBoard(Long id, String tokenValue) {
        Board board = findBoard(id);
        String username = board.getUser().getUsername();

        MessageResponseDto message = new MessageResponseDto("게시물 삭제를 성공했습니다.", HttpStatus.OK.value());
        if (!username.equals(tokenUsername(tokenValue))) {
            if (findUser(tokenUsername(tokenValue)).getRole() == UserRoleEnum.ADMIN) {
                boardRepository.delete(board);

                return ResponseEntity.status(HttpStatus.OK).body(message);
            }
            throw new IllegalArgumentException("해당 게시물의 작성자만 삭제할 수 있습니다.");
        }
        boardRepository.delete(board);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // 게시물 검색
    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new NullPointerException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    // 유저 검색
    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NullPointerException("해당 유저는 존재하지 않습니다.")
        );
    }

    // 토큰에서 유저네임 가져오기
    private String tokenUsername(String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        Claims info = jwtUtil.getUserInfoFromToken(token);

        return info.getSubject();
    }

}
