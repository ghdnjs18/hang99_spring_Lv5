package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.MessageResponseDto;
import com.sparta.springboottest.dto.SignupRequestDto;
import com.sparta.springboottest.security.UserDetailsImpl;
import com.sparta.springboottest.security.ValidationGroups.ValidationSequence;
import com.sparta.springboottest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<MessageResponseDto> signup(@Validated(ValidationSequence.class) @RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @DeleteMapping("/user/resign")
    public ResponseEntity<MessageResponseDto> resign(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.resign(userDetails.getUser());
    }

}
