package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByModifiedTimeDesc().stream().map(BoardResponseDto::new).toList();
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);

        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);

        if (board.getPassword().equals(requestDto.getPassword())) {
            board.update(requestDto);

        } else {
            // 안맞으면 변경안함.
            // 그래도 게시글은 반환 하는게 맞는데, 다른 메세지는 어떻게?
        }

        return new BoardResponseDto(board);
    }

    public Boolean deleteBoard(Long id, BoardRequestDto requestDto) {
        Board board = findBoard(id);

        if (board.getPassword().equals(requestDto.getPassword())) {
            boardRepository.delete(board);

            return true;
        } else {
            return false;
        }

    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }
}
