package com.sparta.springboottest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "user_use")
    private boolean userUse = true;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Comment> commentList = new ArrayList<>();

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void addCommentList(Comment comment) {
        this.commentList.add(comment);
    }

    public void addBoardList(Board board) {
        this.boardList.add(board);
    }
}
