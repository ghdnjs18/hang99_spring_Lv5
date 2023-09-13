package com.sparta.springboottest.service;

import com.sparta.springboottest.dto.CategoryRequestDto;
import com.sparta.springboottest.dto.CategoryResoponseDto;
import com.sparta.springboottest.entity.Category;
import com.sparta.springboottest.entity.User;
import com.sparta.springboottest.entity.UserRoleEnum;
import com.sparta.springboottest.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

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
}
