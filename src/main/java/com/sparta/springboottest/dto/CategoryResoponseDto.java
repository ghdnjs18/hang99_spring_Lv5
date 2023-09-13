package com.sparta.springboottest.dto;

import com.sparta.springboottest.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResoponseDto {
    private Long id;
    private String name;

    public CategoryResoponseDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
