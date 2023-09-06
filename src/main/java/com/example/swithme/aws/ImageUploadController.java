//package com.example.swithme.aws;
//
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor // Lombok 라이브러리, final 필드에 대한 생성자를 생성
//public class ImageUploadController {
//
//    private final ImageUploadService imageUploadService;
//
//    @PostMapping("/api/post/image/upload")
//    public String uploadImage(@RequestPart MultipartFile file) {
//        return imageUploadService.uploadImage(file);
//    }
//}
