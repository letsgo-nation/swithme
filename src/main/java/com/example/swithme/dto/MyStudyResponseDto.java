package com.example.swithme.dto;

import com.example.swithme.entity.Comment;
import com.example.swithme.entity.MyStudy;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MyStudyResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long category_id;
    private Long user_id;
    private String username;
    private String userNickname;
    private List<CommentResponseDto> commentResponseDtoList;

    public MyStudyResponseDto(MyStudy myStudy) {
        this.id = myStudy.getId();
        this.title = myStudy.getTitle();
        this.content = myStudy.getContent();
        this.createdAt = myStudy.getCreatedAt();
        this.modifiedAt = myStudy.getModifiedAt();
        this.category_id = myStudy.getCategory().getId();
        this.user_id = myStudy.getUser().getUserId(); // MyStudy ->  User -> userId
        this.username = myStudy.getUser().getUsername(); // MyStudy ->  User -> username
        this.userNickname = myStudy.getUser().getNickname();
        if (myStudy.getCommentList().size() > 0) {
            this.commentResponseDtoList = new ArrayList<>();
            for (Comment comment : myStudy.getCommentList()) {
                this.commentResponseDtoList.add(new CommentResponseDto(comment));
            }
        }// end of the if()
    }// end of constructor method()
}
