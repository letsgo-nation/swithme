package com.example.swithme.dto;

import com.example.swithme.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(Category category) {
        this.id = category.getCategoryId();
        this.name = category.getName();
    }
}
