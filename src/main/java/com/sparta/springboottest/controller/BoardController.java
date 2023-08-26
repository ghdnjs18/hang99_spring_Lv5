package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.service.BoardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/create")
    public HashMap<String, String> createBoard(@RequestBody BoardRequestDto requestDto) {
        BoardResponseDto board = boardService.createBoard(requestDto);
        HashMap<String, String> map = new HashMap<>();

        map.put("title", board.getTitle());
        map.put("username", board.getUsername());
        map.put("contents", board.getContents());
        map.put("modifiedTime", board.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss")));

        return map;
    }

}
