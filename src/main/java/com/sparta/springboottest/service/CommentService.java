package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.CommentRequestDto;
import com.sparta.springboottest.dto.CommentResponseDto;
import com.sparta.springboottest.dto.MessageResponseDto;
import com.sparta.springboottest.entity.*;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.CommentLikeRepository;
import com.sparta.springboottest.repository.CommentRepository;
import com.sparta.springboottest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Long boardId = requestDto.getBoardId();
        Board board = findBoard(boardId);

        String username = user.getUsername();
        User user_selcet = findUser(username);

        Comment comment = new Comment(requestDto);
        comment.setUsername(username);

        board.addCommentList(comment);
        user_selcet.addCommentList(comment);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(id);

        if (!user.getUsername().equals(comment.getUsername()) && user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("해당 댓글의 작성자만 수정할 수 있습니다.");
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    public ResponseEntity<MessageResponseDto> deleteComment(Long id, User user) {
        Comment comment = findComment(id);

        if (!user.getUsername().equals(comment.getUsername()) && user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("해당 댓글의 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
        MessageResponseDto message = new MessageResponseDto("게시물 삭제를 성공했습니다.", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> likeComment(Long id, User user) {
        Comment comment = findComment(id);
        User userSelect = findUser(user.getUsername());
        CommentLike commentLike = commentLikeRepository.findByUser_IdAndComment_Id(userSelect.getId(), id);

        if (commentLike == null) {
            commentLike = commentLikeRepository.save(new CommentLike(user, comment));
            comment.addCommentLikeList(commentLike);
        }

        MessageResponseDto message;
        if (commentLike.isCheck()) {
            commentLike.setCheck(false);
            comment.setCommentLike(comment.getCommentLike() + 1);
            message = new MessageResponseDto("게시물 좋아요를 성공했습니다.", HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }

        commentLike.setCheck(true);
        comment.setCommentLike(comment.getCommentLike() - 1);
        message = new MessageResponseDto("게시물 좋아요를 취소했습니다.", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("선택한 댓글은 존재하지 않습니다.")
        );
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new NullPointerException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NullPointerException("해당 유저는 존재하지 않습니다.")
        );
    }
}
