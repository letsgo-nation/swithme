package com.example.swithme.controller;

import com.example.swithme.dto.*;
import com.example.swithme.dto.user.SignupRequestDto;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GoogleService;
import com.example.swithme.service.KakaoService;
import com.example.swithme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtUtil jwtUtil;

    // 회원가입 페이지 이동
    @GetMapping("/users/signup")
    public String signUp(Model model) {
        model.addAttribute("signupRequestDto", new SignupRequestDto());
        return "user/signUp";
    }

    //회원가입
    @PostMapping("/api/users/signup")
    public String signup(@Validated @ModelAttribute SignupRequestDto signupRequestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //검증 오류 확인 후 에러 발생시 리턴
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/signUp";
        }
        //특정 조건 오류 검증 후 실패시 리턴
        userService.signup(signupRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/signUp";
        }
        return "user/login";
    }

    // 로그인 페이지 이동
    @GetMapping("/users/login")
    public String loginPage() {
        return "/user/login";
    }

    //일반 로그인
    @PostMapping("/api/users/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse response) {
        userService.login(requestDto,response);
        return "redirect:/";
    }

    //카카오 로그인
    @GetMapping("/api/users/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.kakaoLogin(code, response); // 쿠키 생성
        return "redirect:/";

    }
    //구글 로그인
    @GetMapping("/api/users/google/callback")
    public String googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        googleService.googleLogin(code, response); // 반환 값이 JWT 토큰
        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("/users/logout")
    public String logout(HttpServletResponse response) {
        jwtUtil.expireCookie(response);
        return "redirect:/";
    }

    @GetMapping("/users/profile")
    @ResponseBody
    public UserResponseDto lookupUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.lookupUser(userDetails.getUser().getUserId());
    }

    @PutMapping("/users/profile/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> updateUser(@PathVariable Long userId, @RequestBody UpdateRequestDto updateRequestDto) {
        return userService.updateUser(userId, updateRequestDto);
    }

    @DeleteMapping("/users/profile/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deletePost(userId, userDetails.getUser());
    }
}