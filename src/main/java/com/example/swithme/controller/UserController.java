package com.example.swithme.controller;

import com.example.swithme.dto.user.*;
import com.example.swithme.entity.User;
import com.example.swithme.jwt.JwtUtil;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GoogleService;
import com.example.swithme.service.KakaoService;
import com.example.swithme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        // 로그인 실패시
        try {
            userService.login(requestDto,response);
        } catch (IllegalArgumentException e) {
            e.getMessage();
            return "redirect:/users/login?error=true";
        }

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

    //로그아웃
    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        userService.logout(tokenValue);
        jwtUtil.expireCookie(response);
        return "redirect:/";
    }

    //마이 페이지 이동
    @GetMapping("/users/myPage")
    public String myPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        if((user.getGoogleId() == null && user.getKakaoId() == null)) {
            model.addAttribute("userUpdateRequestDto", new UserUpdateRequestDto(user));
            return "user/myPage";
        }
        model.addAttribute("userUpdateRequestDto", new UserUpdateRequestDto(user));
        return "user/myPageOauth2Login";
    }

    //일반 로그인 정보 수정(닉네임, 이메일)
    @PostMapping("/api/users/myPage")
    public String updateUser(@Validated @ModelAttribute UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/myPage";
        }

        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userUpdateRequestDto);
        model.addAttribute("userUpdateRequestDto", userUpdateResponseDto);
        return "user/myPage";
    }

    //일반 로그인 정보 수정(닉네임, 이메일)
    @PostMapping("/api/users/Oauth2myPage")
    public String updateOauth2User(@Validated @ModelAttribute UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/myPageOauth2Login";
        }

        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userUpdateRequestDto);
        model.addAttribute("userUpdateRequestDto", userUpdateResponseDto);
        return "user/myPageOauth2Login";
    }

    //일반 로그인 비밀번호 변경 페이지 이동
    @GetMapping("/users/updatePassword")
    public String updateMovePassword(Model model) {
        model.addAttribute("passwordRequestDto", new PasswordRequestDto());
        return "user/updatePassword";
    }

    //비밀번호 변경
    @PostMapping("/api/users/updatePassword")
    public String updatePassword(@Validated @ModelAttribute PasswordRequestDto passwordRequestDto, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "user/updatePassword";
        }
        userService.updatePassword(passwordRequestDto, userDetails.getUser(), bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/updatePassword";
        }
        return "redirect:/?success=password";
    }

    //회원탈퇴 페이지로 이동
    @GetMapping("/users/delete")
    public String moveDeleteUser(@AuthenticationPrincipal  UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("username", user.getUsername());
        return "user/deleteUser";
    }

    //회원탈퇴
    @PostMapping("/api/users/delete")
    public String deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @ModelAttribute PasswordRequestDto passwordRequestDto, Model model, HttpServletResponse response) {
        User user = userDetails.getUser();
        if (userService.checkPassword(user, passwordRequestDto)) {
            userService.delete(user);
            jwtUtil.expireCookie(response);
            return "redirect:/?success=delete";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("error", "비밀번호를 확인해주세요");
        return "user/deleteUser";
    }
    @GetMapping("/user/accumulated_id")
    public String getaccumulatedtime() {

        return null;
    }
}