package com.example.swithme.dto;

import com.example.swithme.entity.Comment;
import com.example.swithme.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
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

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.category_id = post.getCategory().getId();
        this.user_id = post.getUser().getUserId(); // post ->  User -> userId
        this.username = post.getUser().getUsername(); // post ->  User -> username
        this.userNickname = post.getUser().getNickname();
        if (post.getCommentList().size() > 0) {
            this.commentResponseDtoList = new ArrayList<>();
            for (Comment comment : post.getCommentList()) {
                this.commentResponseDtoList.add(new CommentResponseDto(comment));
            }
        }// getCommentList if() 끝
    }// end of constructor method()
}
