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

    @GetMapping("/search/title/{title}")
    public List<BoardResponseDto> getBoardTitle(@PathVariable String title) {
        List<BoardResponseDto> board = boardService.getBoardTitle(title);
        return board;
    }

    @GetMapping("/search/username/{username}")
    public List<BoardResponseDto> getBoardUsername(@PathVariable String username) {
        List<BoardResponseDto> board = boardService.getBoardUsername(username);
        return board;
    }

    @GetMapping("/search/contents/{contents}")
    public List<BoardResponseDto> getBoardContents(@PathVariable String contents) {
        List<BoardResponseDto> board = boardService.getBoardContents(contents);
        return board;
    }

    @GetMapping("/search/modifiedTime/{modifiedTime}")
    public List<BoardResponseDto> getBoardModifiedTime(@PathVariable LocalDateTime modifiedTime) {
        List<BoardResponseDto> board = boardService.getBoardModifiedTime(modifiedTime);
        return board;
    }

    @PutMapping("/{id}/pw/{password}")
    public HashMap<String, String> updateBoard(@PathVariable Long id, @PathVariable String password, @RequestBody BoardRequestDto requestDto) {
        return getJsonFormat(boardService.updateBoard(id, password, requestDto));
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
