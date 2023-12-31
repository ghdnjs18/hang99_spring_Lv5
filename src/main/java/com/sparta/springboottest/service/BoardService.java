package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.BoardRequestDto;
import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.dto.MessageResponseDto;
import com.sparta.springboottest.entity.*;
import com.sparta.springboottest.repository.BoardLikeRepository;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.CategoryRepository;
import com.sparta.springboottest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CategoryRepository categoryRepository;

    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto);
        String username = user.getUsername();
        board.setUsername(username);

        Category category = findCategory(requestDto.getCategoryId());
        category.addBoardList(board);

        User userSelect = findUser(username);
        userSelect.addBoardList(board);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> getBoards(int page, int size, String sortBy, boolean isAsc) {
        // 페이지 정렬 선언
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BoardResponseDto> pageList = boardRepository.findAllByBoardUseTrue(pageable).map(BoardResponseDto::new);
        for(BoardResponseDto boardResponseDto : pageList){
            commentChange(boardResponseDto);
        }

        return pageList;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long id) {
        Board board = findBoard(id);
        BoardResponseDto responseDto = new BoardResponseDto(board);
        commentChange(responseDto);

        return responseDto;
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        Board board = findBoard(id);

        if (!user.getUsername().equals(board.getUsername()) && user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("해당 게시물의 작성자만 수정할 수 있습니다.");
        }
        board.update(requestDto);
        return new BoardResponseDto(board);
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> deleteBoard(Long id, User user) {
        Board board = findBoard(id);

        if (!user.getUsername().equals(board.getUsername()) && user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("해당 게시물의 작성자만 삭제할 수 있습니다.");
        }

        board.setBoardUse(false);
        for (Comment comment : board.getCommentList()) {
            comment.setCommentUse(false);
        }

        MessageResponseDto message = new MessageResponseDto("게시물 삭제를 성공했습니다.", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> likeBoard(Long id, User user) {
        Board board = findBoard(id);
        User userSelect = findUser(user.getUsername());
        BoardLike boardLike = boardLikeRepository.findByUser_IdAndBoard_Id(userSelect.getId(), id);

        if (boardLike == null) {
            boardLike = boardLikeRepository.save(new BoardLike(user, board));
            board.addBoardLikeList(boardLike);
        }

        MessageResponseDto message;
        if (boardLike.isCheck()) {
            boardLike.setCheck(false);
            board.setBoardLike(board.getBoardLike() + 1);
            message = new MessageResponseDto("게시물 좋아요를 성공했습니다.", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }

        boardLike.setCheck(true);
        board.setBoardLike(board.getBoardLike() - 1);
        message = new MessageResponseDto("게시물 좋아요를 취소했습니다.", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // 게시물 검색
    private Board findBoard(Long id) {
        return boardRepository.findByIdAndBoardUseTrue(id).orElseThrow(() ->
                new NullPointerException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    // 유저 검색
    private User findUser(String username) {
        return userRepository.findByUsernameAndUserUseTrue(username).orElseThrow(() ->
                new NullPointerException("해당 유저는 존재하지 않습니다.")
        );
    }

    // 카테고리 검색
    private Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 카테고리는 존재하지 않습니다.")
        );
    }

    // 삭제된 댓글 응답
    private void commentChange(BoardResponseDto boardResponseDto) {
        for (Comment comment : boardResponseDto.getCommentList()) {
            commentSetChange(comment);
        }
    }

    // 대댓글 삭제 확인
    private void commentSetChange(Comment comment) {
        if (!comment.isCommentUse()) {
            comment.setUsername("알수없음");
            comment.setComment("삭제된 댓글입니다.");
        }
        if (comment.getChildcommentList() != null) {
            for (Comment comment1 : comment.getChildcommentList()) {
                commentSetChange(comment1);
            }
        }
    }
}
