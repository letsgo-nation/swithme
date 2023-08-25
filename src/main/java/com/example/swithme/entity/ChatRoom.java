package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;

    private String content;

    private String category;

    private UUID url; // 채팅방 입장 주소

    private String img; // 이미지 고민

    public ChatRoom(String title, String content, String category, UUID url) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.url = url;
    }
}
