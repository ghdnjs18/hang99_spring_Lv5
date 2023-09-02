package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.LoginRequestDto;
import com.sparta.springboottest.dto.SignupRequestDto;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.jwt.JwtUtil;
import com.sparta.springboottest.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public ResponseEntity<Map> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(makeJson("회원가입이 성공했습니다."));
    }

    // 로그인
    public ResponseEntity<Map> login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response  객체에 추가
        String token = jwtUtil.createToken(user.getUsername());
        jwtUtil.addJwtToCookie(token, res);

        return ResponseEntity.status(HttpStatus.OK).body(makeJson("로그인 성공했습니다."));
    }

    // 성공 메시지 생성
    private Map<String, String> makeJson(String message) {
        Map<String, String> map = new HashMap();
        map.put("msg", message);
        map.put("statusCode", String.valueOf(HttpStatus.OK).substring(0, 3));

        return map;
    }
}