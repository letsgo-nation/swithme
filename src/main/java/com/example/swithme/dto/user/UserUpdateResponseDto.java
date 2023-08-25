package com.example.swithme.dto.user;

import com.example.swithme.entity.User;
import lombok.Getter;

@Getter
public class UserUpdateResponseDto {
    private Long userId;
    private String username;
    private String nickname;

    public UserUpdateResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}