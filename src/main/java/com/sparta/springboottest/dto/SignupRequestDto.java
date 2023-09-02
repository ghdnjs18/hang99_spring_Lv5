package com.sparta.springboottest.dto;

import com.sparta.springboottest.security.ValidationGroups.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @Size(min = 4, max = 10, message = "username은 4글자 이상, 10글자 이하입니다.", groups = SizeCheckGroup.class)
    @Pattern(regexp = "[a-z0-9]", message = "username형식은 알파벳 소문자와 숫자만 가능합니다.", groups = PatternCheckGroup.class)
    private String username;

    @Size(min = 8, max = 15, message = "password은 8글자 이상, 15글자 이하입니다.", groups = SizeCheckGroup.class)
    @Pattern(regexp = "[a-zA-Z0-9]", message = "password형식은 영문, 숫자, 특수문자만 가능합니다.", groups = PatternCheckGroup.class)
    private String password;
}
