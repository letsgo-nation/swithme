package com.example.swithme.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @Pattern(regexp = "^[A-Za-z0-9]{4,}$", message = "아이디는 최소 4글자 이상, 영문, 숫자 조합이어야 합니다.")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9]{4,}$", message = "비밀번호는 최소 4글자 이상, 영문, 숫자 조합이어야 합니다.")
    private String password;

    //비밀번호 확인
    @Pattern(regexp = "^[A-Za-z0-9]{4,}$", message = "비밀번호는 최소 4글자 이상, 영문, 숫자 조합이어야 합니다.")
    private String checkPassword;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;

}
