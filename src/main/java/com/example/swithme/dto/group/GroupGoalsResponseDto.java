package com.example.swithme.dto.group;

import com.example.swithme.entity.GroupDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupGoalsResponseDto {
    private Long id;
    private String groupGoals;

    public GroupGoalsResponseDto(GroupDetails groupDetails) {
        this.id = groupDetails.getId();
        this.groupGoals = groupDetails.getGroupGoals();
    }
}
