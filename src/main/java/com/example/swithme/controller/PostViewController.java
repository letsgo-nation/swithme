package com.example.swithme.controller;

import com.example.swithme.dto.PostResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.PostService;
import com.example.swithme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view")
public class PostViewController {

    private final PostService postService;
    private final UserService userService;

    // 전체 게시물 페이지
    @GetMapping("/posts")
    public String postsPage(){
        return "post/posts";
    }

    // 게시물 작성 페이지 - 작성자 닉네임 가져오기
    @GetMapping("/post/write")
    public String writePage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("info_username", userDetails.getUser().getNickname());
        return "post/postWrite";
    }

    // 게시글 상세 페이지
    @GetMapping("/post/detail/{id}")
    public String postDetailPage(@PathVariable Long id, Model model) {
        PostResponseDto result = postService.lookupPost(id);
        model.addAttribute("post", result);
        return "post/postDetail";
    }
//
//    // 게시글 수정 페이지
//    @GetMapping("/post/modify/{id}")
//    public String modifyPost(Model model,  @PathVariable Long id)
//            throws JsonProcessingException {
//        model.addAttribute("info_post",postService.getPost(id));
//        return "postModify";
//    }

}
