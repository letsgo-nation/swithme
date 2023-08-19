package com.example.swithme.service;

import com.example.swithme.dto.*;
import com.example.swithme.dto.user.SignupRequestDto;
import com.example.swithme.entity.User;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.repository.TokenBlacklistRepository;
import com.example.swithme.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final TokenBlacklistRepository tokenBlacklistRepository;

  //회원가입
  public void signup(SignupRequestDto signupRequestDto, BindingResult bindingResult) {
    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    String checkPassword = signupRequestDto.getCheckPassword();
    String email = signupRequestDto.getEmail();
    String nickName = signupRequestDto.getNickName();

    //닉네임과 같은 값이 비밀번호에 포함된 경우 회원가입 실패
    boolean usernameCheck = password.contains(username);
    if (usernameCheck) {
      bindingResult.addError(new FieldError("signupRequestDto", "password", "비밀번호에 유저네임을 포함할 수 없습니다."));
    }

    //가입시 비밀번호 입력, 비밀번호 확인 같은지 확인
    if (!password.equals(checkPassword)) {
      bindingResult.addError(new FieldError("signupRequestDto", "password", "비밀번호가 일치하지 않습니다."));
    }

    //회원 중복 확인
    Optional<User> checkUsername = userRepository.findByUsername(username);
    if (checkUsername.isPresent()) {
      bindingResult.addError(new FieldError("signupRequestDto", "username", "중복된 사용자가 존재합니다."));
    }

    //에러 없을 때 회원가입 성공
    if (!bindingResult.hasErrors()) {
      String passwordEncode = passwordEncoder.encode(signupRequestDto.getPassword());
      User user = new User(username, passwordEncode, email, nickName);
      userRepository.save(user);
    }
  }

  public ResponseEntity<String> login(LoginRequestDto requestDto, HttpServletResponse response) {
    String username = requestDto.getUsername();
    String password = requestDto.getPassword();

    Optional<User> checkUser = userRepository.findByUsername(username);

    if (!checkUser.isPresent() || !passwordEncoder.matches(password,
            checkUser.get().getPassword())) {
      throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
    }

    String token = jwtUtil.createToken(requestDto.getUsername());
    jwtUtil.addJwtToCookie(token, response);

    return ResponseEntity.ok().body("로그인 성공");
  }

  public UserResponseDto lookupUser(Long userId) {
    User user = findUser(userId);
    return new UserResponseDto(user);
  }

  @Transactional
  public ResponseEntity<ApiResponseDto> updateUser(Long userId, UpdateRequestDto updateRequestDto) {
    // DB 에서 해당 유저 가져오기
    Optional<User> inputUpdateUser = userRepository.findById(userId);

    if (!inputUpdateUser.isPresent()) {
      return ResponseEntity.status(400)
              .body(new ApiResponseDto("해당 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
    }

    // 비밀번호와 확인 비밀번호 일치 여부 판단
    if (!updateRequestDto.getPassword().equals(updateRequestDto.getCheckPassword())) {
      return ResponseEntity.status(400)
              .body(new ApiResponseDto("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
    }

    String password = passwordEncoder.encode(updateRequestDto.getPassword());
    User user = inputUpdateUser.get();
    user.update(updateRequestDto, password);

    // User -> UserResponseDto
    UserResponseDto userResponseDto = new UserResponseDto(user);

    ApiResponseDto result = new ApiResponseDto(HttpStatus.OK.value(), "프로필 수정 성공", userResponseDto);

    return ResponseEntity.status(200).body(result);
  }

  public ResponseEntity<ApiResponseDto> deletePost(Long userId, User user) {
    Optional<User> inputDeleteUser = userRepository.findById(userId);

    if (!inputDeleteUser.isPresent()) {
      return ResponseEntity.status(400)
              .body(new ApiResponseDto("해당 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
    }
    deleteUser(user);
    userRepository.delete(user);
    return ResponseEntity.status(400)
            .body(new ApiResponseDto("탈퇴가 완료되었습니다.", HttpStatus.BAD_REQUEST.value()));
  }

  private void deleteUser(User user) {
    userRepository.delete(user);
  }

  private User findUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
    );
  }
}