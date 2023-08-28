package com.example.swithme.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoomRequestDto {
    private Long id;

    private String title;

    private String content;

    private String category; // 카테고리에 따라 들어가는 방 주소가 변경됨

    private String img; // 이미지 고민

    private UUID chatUrl;
}
