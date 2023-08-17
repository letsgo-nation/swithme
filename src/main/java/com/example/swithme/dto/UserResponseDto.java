package com.example.swithme.dto;

import com.example.swithme.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long userId;
    private String username;
    private String nickname;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}