package com.example.swithme.entity;

import com.example.swithme.dto.group.GroupDetailsRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "groupDetails")
public class GroupDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupGoals;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public GroupDetails(GroupDetailsRequestDto requestDto, Group group, User user) {
        this.groupGoals = requestDto.getGroupGoals();
        this.user = user;
        this.group = group;
    }
}
