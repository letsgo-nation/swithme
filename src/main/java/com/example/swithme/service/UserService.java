package com.example.swithme.service;

import com.example.swithme.dto.*;
import com.example.swithme.entity.TokenBlacklist;
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

  public ResponseEntity<String> signup(SignupRequestDto requestDto) {
    String username = requestDto.getUsername();
    String password = passwordEncoder.encode(requestDto.getPassword());
    String nickname = requestDto.getNickname();

    if (Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            requestDto.getUsername()) && Pattern.matches("^[a-z0-9]{4,10}$",
            requestDto.getPassword())) {
      Optional<User> checkUsername = userRepository.findByUsername(username);
      if (checkUsername.isPresent()) {
        throw new IllegalArgumentException("중복된 username 입니다.");
      } else {
        User user = new User(username, password, nickname);
        userRepository.save(user);

        return ResponseEntity.ok().body("회원가입 성공");
      }
    }
    throw new IllegalArgumentException("username, password 형식에 맞춰 작성해주세요.");
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