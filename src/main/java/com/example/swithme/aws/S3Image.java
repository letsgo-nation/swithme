//package com.example.swithme.aws;
//
//import com.example.swithme.entity.Post;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Entity
//@NoArgsConstructor
//@Table(name = "s3Images")
//public class S3Image {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long imageId;
//
//    @Column(length = 255)
//    private String fileName;
//
//    @ManyToOne(fetch= FetchType.LAZY)
//    @JoinColumn(name = "Post_Id", nullable = false)
//    private Post post;
//
//    public S3Image(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public S3Image(String fileName, Post post) {
//        this.fileName = fileName;
//        this.post = post;
//    }
//}
