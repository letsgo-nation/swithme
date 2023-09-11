package com.example.swithme.service;

import com.example.swithme.dto.group.GroupDetailsRequestDto;
import com.example.swithme.dto.group.GroupGoalsResponseDto;
import com.example.swithme.entity.Group;
import com.example.swithme.entity.GroupDetails;
import com.example.swithme.entity.User;
import com.example.swithme.repository.GroupDetailsRepository;
import com.example.swithme.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupGoalsService {

    private final GroupDetailsRepository groupDetailsRepository;
    private final GroupRepository groupRepository;

    public List<GroupGoalsResponseDto> getGoals(Long groupId) {
        List<GroupDetails> groupDetailsList = groupDetailsRepository.findAllByGroupId(groupId);
        List<GroupGoalsResponseDto> GroupGoalsResponseDtoList = new ArrayList<>();

        for (GroupDetails groupDetails : groupDetailsList) {
            GroupGoalsResponseDtoList.add(new GroupGoalsResponseDto(groupDetails));
        }
        return GroupGoalsResponseDtoList;
    }

    public GroupGoalsResponseDto createGroupGoals(Long groupId, GroupDetailsRequestDto requestDto, User user) {
        Optional<Group> group = groupRepository.findById(groupId);

        if(!group.isPresent()) {
            log.error("해당 그룹이 없습니다.");
            return null;
        }

        GroupDetails groupDetails = new GroupDetails(requestDto, group.get(),user);

        groupDetailsRepository.save(groupDetails);
        return new GroupGoalsResponseDto(groupDetails);
    }
}
