package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.BoardResponseDto;
import com.sparta.springboottest.dto.CategoryRequestDto;
import com.sparta.springboottest.dto.CategoryResoponseDto;
import com.sparta.springboottest.security.UserDetailsImpl;
import com.sparta.springboottest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public CategoryResoponseDto createCategory(@RequestBody CategoryRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.createCategory(requestDto, userDetails.getUser());
    }

    @GetMapping("/category/{id}")
    public Page<BoardResponseDto> getCategoryBoards(
            @PathVariable Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc) {
        return categoryService.getCategoryBoards(id, page-1, size, sortBy, isAsc);
    }
}
