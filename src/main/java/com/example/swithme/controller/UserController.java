package com.example.swithme.controller;

import com.example.swithme.dto.*;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GoogleService;
import com.example.swithme.service.KakaoService;
import com.example.swithme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse response) {
        userService.login(requestDto,response);
        return "index";
    }

    @GetMapping("/profile")
    @ResponseBody
    public UserResponseDto lookupUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.lookupUser(userDetails.getUser().getUserId());
    }

    @PutMapping("/profile/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> updateUser(@PathVariable Long userId, @RequestBody UpdateRequestDto updateRequestDto) {
        return userService.updateUser(userId, updateRequestDto);
    }

    @DeleteMapping("/profile/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deletePost(userId, userDetails.getUser());
    }

    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response); // 쿠키 생성
        return "redirect:/";
    }

    @GetMapping("/google/callback")
    public String googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = googleService.googleLogin(code); // 반환 값이 JWT 토큰

        token = token.substring(7);
        token = "Bearer%20" + token;
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER,token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    //권한 설정
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/secured")
    public String securedPage() {
        return "This is a secured page.";
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        userService.logout(token);
        return ResponseEntity.ok().body(new ApiResponseDto("로그아웃 되었습니다.", HttpStatus.OK.value()));
    }


    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}