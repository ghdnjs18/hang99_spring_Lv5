package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.dto.CategoryRequestDto;
import com.sparta.springboottest.dto.CategoryResoponseDto;
import com.sparta.springboottest.entity.Category;
import com.sparta.springboottest.entity.Comment;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.entity.UserRoleEnum;
import com.sparta.springboottest.repository.BoardRepository;
import com.sparta.springboottest.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    public CategoryResoponseDto createCategory(CategoryRequestDto requestDto, User user) {
        if (user.getRole() == UserRoleEnum.USER) {
            throw new IllegalArgumentException("카테고리는 관리자만 생성 가능합니다.");
        }
        Category existCategory = categoryRepository.findByName(requestDto.getName()).orElse(null);
        if (existCategory != null) throw new DataIntegrityViolationException("중복된 카테고리명입니다.");

        Category category = new Category(requestDto.getName());
        categoryRepository.save(category);

        return new CategoryResoponseDto(category);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> getCategoryBoards(Long id, int page, int size, String sortBy, boolean isAsc) {
        // 페이지 정렬 선언
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Category category = findCategory(id);

        Page<BoardResponseDto> pageList = boardRepository.findAllByCategoryAndBoardUseTrue(category, pageable).map(BoardResponseDto::new);
        for(BoardResponseDto boardResponseDto : pageList){
            commentChange(boardResponseDto);
        }

        return pageList;
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
