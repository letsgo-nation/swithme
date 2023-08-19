package com.example.swithme.controller;

import com.example.swithme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {




    @ResponseBody
    @GetMapping("/board")
    public String board(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();

        return username;
    }


}
