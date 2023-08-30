package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        BoardResponseDto dto = boardService.createBoard(requestDto);
        return dto;
    }

    @GetMapping("/board")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(id, requestDto);
    }

    @DeleteMapping("/board/{id}")
    public HashMap<String, String> deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        HashMap<String, String> map = new HashMap<>();

        if (boardService.deleteBoard(id, requestDto)) {
            map.put("success", "성공");
        }
        if (!boardService.deleteBoard(id, requestDto)) {
            map.put("success", "실패");
        }

        return map;
    }
}
