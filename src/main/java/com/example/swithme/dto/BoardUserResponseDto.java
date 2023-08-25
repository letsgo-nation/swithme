package com.example.swithme.dto;

import com.example.swithme.entity.Group;
import com.example.swithme.entity.GroupUser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class BoardUserResponseDto {

  private Long id; // 보드 번호
  private String groupName; // 보드 이름
  private String description; // 보드 설명
  private Set<UserResponseDto> users; // 유저정보
//  private List<UserResponseDto> userList = new ArrayList<>();

  public BoardUserResponseDto(Group group) {
    this.id = group.getId();
    this.groupName = group.getGroupName();
    this.description = group.getDescription();
    this.users = group.getGroupUsers().stream()
            .map(user -> new UserResponseDto(user.getUser()))
            .collect(Collectors.toSet());

//    if (group.getGroupUsers().size() > 0) {
//      for (GroupUser groupUser : group.getGroupUsers()) {
//        this.getUserList().add(new UserResponseDto(groupUser.getUser()));
//      }
//    }
  }

}