package com.example.swithme.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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

    private String category; // 카테고리에 따라 들어가는 방 주소가 변경됨

    private String img; // 이미지 고민

    public ChatRoom(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
