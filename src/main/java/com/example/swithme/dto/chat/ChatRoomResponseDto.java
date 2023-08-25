package com.example.swithme.dto.chat;

import com.example.swithme.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoomResponseDto {

    private String title;

    private String content;

    private String category; // 카테고리에 따라 들어가는 방 주소가 변경됨

    private String img; // 이미지 고민

    private UUID url;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.title = chatRoom.getTitle();
        this.content = chatRoom.getContent();
        this.category = chatRoom.getCategory();
        this.img = chatRoom.getImg();
        this.url = chatRoom.getUrl();
    }
}
