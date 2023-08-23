package com.example.swithme.controller;

import com.example.swithme.dto.MyStudyResponseDto;
import com.example.swithme.entity.UserRoleEnum;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Secured(UserRoleEnum.Authority.ADMIN) // 관리자만 접근 가능
public class ManageController {

    ManagerService managerService;

    @GetMapping("/secured")
    public String getProductsByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            System.out.println("authority.getAuthority() = " + authority.getAuthority());
        }
        return "user/manager";
    }

    // 전체 개인 스터디 게시물 조회
    @GetMapping("/secured/myStudies")
    public String getMyStudies() {
        managerService.findAll();
        return "user/manager";
    }
}
