package com.example.swithme.dto.user;
import com.example.swithme.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    @Pattern(regexp = "^[A-Za-z0-9]{4,15}$", message = "아이디는 4~15 글자의 영문 숫자 조합이어야 합니다.")
    private String username;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String email;

    public UserUpdateRequestDto(User user) {
        this.username = user.getUsername();
        this.nickName = user.getNickname();
    }
}