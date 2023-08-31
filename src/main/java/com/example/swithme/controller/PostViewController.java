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
import org.springframework.web.bind.annotation.*;

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

    // 게시물 상세 조회 - 게시물, 댓글, 대댓글 조회 / 게시물 삭제
    @GetMapping("/post/detail/{id}")
    public String postDetailPage(@PathVariable Long id, Model model) {
        PostResponseDto result = postService.lookupPost(id);
        model.addAttribute("post", result);
        model.addAttribute("commentList", result.getCommentResponseDtoList());
        return "post/postDetail";
    }
    // 게시글 수정 페이지
    @GetMapping("/post/update/{id}")
    public String updatePostView(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(!userService.lookupUser(postService.lookupPost(id).getUser_id()).getUsername().equals(userDetails.getUsername())) {
            /* 게시글 작성자가 아닐 시, id에 해당하는 게시글 페이지로 이동 */
            return "redirect:/view/post/detail/"+id;
        }
        model.addAttribute("username",userDetails.getUser().getUsername());
        model.addAttribute("post",postService.lookupPost(id));
        return "post/postUpdate";
    }
}
