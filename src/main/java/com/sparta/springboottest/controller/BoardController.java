package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.service.BoardService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public HashMap<String, String> createBoard(@RequestBody BoardRequestDto requestDto) {
        return getJsonFormat(boardService.createBoard(requestDto));
    }

    @GetMapping("/search")
//    private List<HashMap<String, String>> getBoard() {
    public List<BoardResponseDto> getBoard() {
        List<BoardResponseDto> board = boardService.getBoard();
        return board;
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
