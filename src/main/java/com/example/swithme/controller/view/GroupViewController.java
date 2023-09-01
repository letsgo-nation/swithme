package com.example.swithme.controller.view;

import com.example.swithme.dto.BoardResponseDto;
import com.example.swithme.dto.PostResponseDto;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GroupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/view")
@AllArgsConstructor
public class GroupViewController {

    private final GroupService groupService;

    @GetMapping("/creategroup")
    public String CreateGroup() {
        return "groupstudy/groupstudywrite";
    }

    @GetMapping("/groups")
    public String ViewGroups() {
        return "groupstudy/groupstudy";
    }

    @GetMapping("/group/{groupId}/update")
    public String groupUpdate(@PathVariable Long groupId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        model.addAttribute("info_groupName", userDetails.getUser().getNickname());
        model.addAttribute("info_groupId", groupService.getBoard(groupId).getId());
        return "groupstudy/groupstudyupdate";
    }

    @GetMapping("/group-page/{groupId}")
    public String getPost(@PathVariable Long groupId, Model model) {
        BoardResponseDto responseDto = groupService.getBoard(groupId);
        model.addAttribute("group", responseDto);

        return "groupstudy/groupstudydetails"; // postDetail.html view
    }
}
