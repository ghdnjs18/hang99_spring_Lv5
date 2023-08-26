package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/")
    public HashMap<String, String> createBoard(@RequestBody BoardRequestDto requestDto) {
        return getJsonFormat(boardService.createBoard(requestDto));
    }

    @GetMapping("/")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping("/{id}")
    public HashMap<String, String> updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return getJsonFormat(boardService.updateBoard(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public HashMap<String, String> deleteBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        HashMap<String, String> map = new HashMap<>();

        if (boardService.deleteBoard(id, requestDto) != 0) {
            map.put("success", "성공");
        } else {
            map.put("success", "실패");
        }
        return map;
    }

    private HashMap<String, String> getJsonFormat(BoardResponseDto boardResponseDto) {
        BoardResponseDto board = boardResponseDto;

        HashMap<String, String> map = new HashMap<>();

        map.put("title", board.getTitle());
        map.put("username", board.getUsername());
        map.put("contents", board.getContents());
        map.put("modifiedTime", board.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss")));

        return map;
    }
}
