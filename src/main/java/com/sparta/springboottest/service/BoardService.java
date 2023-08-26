package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);

        boardRepository.save(board);

        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    public List<BoardResponseDto> getBoard() {
        return boardRepository.findAllByOrderByModifiedTimeDesc().stream().map(BoardResponseDto::new).toList();
    }

    public List<BoardResponseDto> getBoardTitle(String title) {
        return boardRepository.findAllByTitleContainingOrderByModifiedTimeDesc(title).stream().map(BoardResponseDto::new).toList();
    }

    public List<BoardResponseDto> getBoardUsername(String username) {
        return boardRepository.findAllByUsernameContainingOrderByModifiedTimeDesc(username).stream().map(BoardResponseDto::new).toList();
    }

    public List<BoardResponseDto> getBoardContents(String contents) {
        return boardRepository.findAllByContentsContainingOrderByModifiedTimeDesc(contents).stream().map(BoardResponseDto::new).toList();
    }

    public List<BoardResponseDto> getBoardModifiedTime(LocalDateTime modifiedTime) {
        return boardRepository.findAllByModifiedTimeBeforeOrderByModifiedTimeDesc(modifiedTime).stream().map(BoardResponseDto::new).toList();
    }
}
