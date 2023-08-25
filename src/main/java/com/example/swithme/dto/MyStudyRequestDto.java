package com.example.swithme.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyStudyRequestDto {
    private String title;
    private String content;
    private Long category_id;
}
