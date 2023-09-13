package com.sparta.springboottest.controller;

import com.sparta.springboottest.dto.CategoryRequestDto;
import com.sparta.springboottest.dto.CategoryResoponseDto;
import com.sparta.springboottest.security.UserDetailsImpl;
import com.sparta.springboottest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public CategoryResoponseDto createCategory(@RequestBody CategoryRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return categoryService.createCategory(requestDto, userDetails.getUser());
    }

}
