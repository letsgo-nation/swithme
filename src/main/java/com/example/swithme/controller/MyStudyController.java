package com.example.swithme.controller;

import com.example.swithme.dto.ApiResponseDto;
import com.example.swithme.dto.MyStudyRequestDto;
import com.example.swithme.dto.MyStudyResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.MyStudyService;
import com.example.swithme.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyStudyController {
    private final MyStudyService myStudyService;
    private final UserService userService;

    // 개인 스터디 게시물 생성
    @PostMapping("/myStudy")
    @ResponseBody
    public MyStudyResponseDto createMyStudy(@RequestBody MyStudyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myStudyService.createMyStudy(requestDto, userDetails.getUser());
    }

    // 전체 개인 스터디 게시물 조회
    @GetMapping("/myStudies")
    @ResponseBody
    public List<MyStudyResponseDto> getMyStudies() {
        return myStudyService.getMyStudies();
    }

    // 개인 스터디 게시물 단건 조회
    @GetMapping("/myStudy/{id}")
    @ResponseBody
    public MyStudyResponseDto lookupMyStudy(@PathVariable Long id) {
        return myStudyService.lookupMyStudy(id);
    }

    // 개인 스터디 게시물 수정
    @PutMapping("/myStudy/{id}")
    public ResponseEntity<ApiResponseDto> updateMyStudy(@PathVariable Long id, @RequestBody MyStudyRequestDto myStudyRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myStudyService.updateMyStudy(id, myStudyRequestDto, userDetails.getUser());
    }

    // 개인 스터디 게시물 삭제
    @DeleteMapping("/myStudy/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> deleteMyStudy(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myStudyService.deleteMyStudy(id, userDetails.getUser());
    }

    // 카테고리별 게시물 조회
    @GetMapping("/myStudies/category/{category_id}")
    public ResponseEntity<ApiResponseDto> getCategoryMyStudies(@PathVariable Long category_id) {
        return myStudyService.getCategoryMyStudies(category_id);
    }
}
