package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.MessageResponseDto;
import com.sparta.springboottest.dto.SignupRequestDto;
import com.sparta.springboottest.entity.Board;
import com.sparta.springboottest.entity.Comment;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.entity.UserRoleEnum;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "A1234";

    // 회원가입
    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 권한 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);

        MessageResponseDto message = new MessageResponseDto("회원가입이 성공했습니다.", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<MessageResponseDto> resign(User user) {
        User userselect = userRepository.findByUsername(user.getUsername()).orElse(null);

        // 회원이 작성한 게시물 삭제 처리
        for (Board board : userselect.getBoardList()) {
            board.setBoardUse(false);
            for (Comment comment : board.getCommentList()) {
                comment.setCommentUse(false);
            }
        }
        // 회원이 작성한 댓글 삭제 처리
        for (Comment comment : userselect.getCommentList()) {
            comment.setCommentUse(false);
        }
        userselect.setUserUse(false);

        MessageResponseDto message = new MessageResponseDto("회원탈퇴가 성공했습니다.", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}