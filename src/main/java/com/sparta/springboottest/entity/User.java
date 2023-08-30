package com.sparta.springboottest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column
//    @Pattern(regexp = "[a-z0-9]{4,10}", message = "유저 형식을 확인하세요.")
    @NotBlank(message = "이름은 필수 값 입니다.")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수 값 입니다.")
//    @Pattern(regexp = "[a-zA-Z0-9]{8,15}", message = "비밀번호 형식을 확인하세요.")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
