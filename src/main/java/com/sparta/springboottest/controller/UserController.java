package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.LoginRequestDto;
import com.sparta.springboottest.dto.SignupRequestDto;
import com.sparta.springboottest.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<Map> signup(@RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/user/login")
    public ResponseEntity<Map> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        return userService.login(requestDto, res);
    }
}