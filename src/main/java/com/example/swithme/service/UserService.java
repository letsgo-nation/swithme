package com.example.swithme.service;

import com.example.swithme.dto.user.*;
import com.example.swithme.entity.TokenBlacklist;
import com.example.swithme.entity.User;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.repository.TokenBlacklistRepository;
import com.example.swithme.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final TokenBlacklistRepository tokenBlacklistRepository;


  // 로그아웃시 토큰 블랙리스트 추가
  @Transactional
  public void logout(String token) {
    TokenBlacklist tokenBlacklist = new TokenBlacklist(token);
    tokenBlacklistRepository.save(tokenBlacklist);
  }

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
      User user = new User(username, passwordEncode, nickName);
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

  //비밀번호 확인
  public Boolean checkPassword(User user, PasswordRequestDto passwordRequestDto) {
    String password = user.getPassword();
    String checkPassword = passwordRequestDto.getPassword();

    return passwordEncoder.matches(checkPassword, password);
  }

  //정보 수정(닉네임, 이메일)
  @Transactional
  public UserUpdateResponseDto updateUser(UserUpdateRequestDto userUpdateRequestDto) {
    Optional<User> findUser = userRepository.findByUsername(userUpdateRequestDto.getUsername());

    User user = findUser.orElseThrow(
            () -> new IllegalArgumentException("일치하는 회원이 없습니다."));

    user.update(userUpdateRequestDto);
    return new UserUpdateResponseDto(user);
  }

  //비밀번호 변경
  @Transactional
  public void updatePassword(PasswordRequestDto passwordRequestDto, User user, BindingResult bindingResult) {
    String username = user.getUsername();
    String password = passwordRequestDto.getPassword();
    String newPassword = passwordRequestDto.getNewPassword();
    System.out.println("newPassword = " + newPassword);
    String passwordCheck = passwordRequestDto.getCheckPassword();

    //현재 비밀번호와 입력한 비밀번호가 틀리면 비밀번호 변경 실패
    if(!passwordEncoder.matches(password, user.getPassword())) {
      bindingResult.addError(new FieldError("passwordRequestDto", "password", "비밀번호를 확인해주세요."));
    }
    //닉네임과 같은 값이 비밀번호에 포함된 경우 비밀번호 변경 실패
    boolean usernameCheck = newPassword.contains(username);
    if (usernameCheck) {
      bindingResult.addError(new FieldError("passwordRequestDto", "newPassword", "비밀번호에 유저네임을 포함할 수 없습니다."));
    }

    //비밀번호 일치여부 확인
    if (!newPassword.equals(passwordCheck)) {
      bindingResult.addError(new FieldError("passwordRequestDto", "newPassword", "비밀번호가 일치하지 않습니다."));
    }

    if(!bindingResult.hasErrors()) {
      User findUser = userRepository.findById(user.getUserId()).get();
      String encode = passwordEncoder.encode(newPassword);
      findUser.setPassword(encode);
    }
  }

  // user 찾기
  public UserUpdateResponseDto lookupUser(Long id) {
    User user = findUser(id);
    return new UserUpdateResponseDto(user);
  }

  private User findUser(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
  }

  //회원탈퇴
  @Transactional
  public void delete(User user) {
    userRepository.delete(user);
  }
}