package com.sparta.springboottest.entity;

import com.sparta.springboottest.dto.BoardRequestDto;
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
@Table(name = "board")
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @Column(name="username",nullable = false)
    private String username;

    @Column(name = "boardlike", nullable = false)
    private int like = 0;

    @OneToMany
    @JoinColumn(name = "board_id")
    @OrderBy("modifiedTime desc")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardLike> boardLikeList = new ArrayList<>();

    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void addCommentList(Comment comment) {
        this.commentList.add(comment);
    }

    public void addBoardLikeList(BoardLike boardLike) {
        this.boardLikeList.add(boardLike);
        boardLike.setBoard(this);
    }

}


