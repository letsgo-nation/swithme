package com.example.swithme.controller;

import com.example.swithme.dto.group.GroupDetailsRequestDto;
import com.example.swithme.dto.group.GroupGoalsResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GroupGoalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupDetailsController {

    private final GroupGoalsService groupGoalsService;

    @GetMapping("/group/goals/{groupId}")
    @ResponseBody
    public List<GroupGoalsResponseDto> lookupGroup(@PathVariable Long groupId) {
        return groupGoalsService.getGoals(groupId);
    }

    @PostMapping("/group/{groupId}/goals")
    @ResponseBody
    public GroupGoalsResponseDto createGroupGoals (@PathVariable Long groupId, @RequestBody GroupDetailsRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return groupGoalsService.createGroupGoals(groupId, requestDto, userDetails.getUser());
    }
}
