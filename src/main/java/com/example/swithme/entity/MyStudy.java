package com.example.swithme.entity;

import com.example.swithme.dto.MyStudyRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "myStudy")
@NoArgsConstructor
public class MyStudy extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50000)
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

//    @OneToMany( mappedBy = "post", cascade = CascadeType.ALL)
//    private List<Comment> commentList = new ArrayList<>();

    public MyStudy(MyStudyRequestDto requestDto, User user, Category category) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.category = category;
    }

//    public void addComment(Comment comment) {
//        this.commentList.add(comment);
//    }

    public void update(MyStudyRequestDto postRequestDto, Category category) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.category = category;
    }
}
