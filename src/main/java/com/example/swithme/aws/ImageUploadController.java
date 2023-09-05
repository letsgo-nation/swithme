package com.example.swithme.controller;

import com.example.swithme.aws.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor // Lombok 라이브러리, final 필드에 대한 생성자를 생성
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/api/post/image/upload")
    public String uploadImage(@RequestPart MultipartFile file) {
        return imageUploadService.uploadImage(file);
    }
}
