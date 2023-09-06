//package com.example.swithme.aws;
//
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectSummary;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/images")
//public class S3ImageController {
//
//    @Autowired
//    private S3Service s3Service;
//
//    @PostMapping("/upload")
//    public void uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("s3Key") String s3Key) throws IOException {
//        s3Service.uploadImage(file, s3Key);
//    }
//
//    @GetMapping("/download")
//    public S3Object downloadImage(@RequestParam("s3Key") String s3Key) {
//        return s3Service.downloadImage(s3Key);
//    }
//
//    @GetMapping("/list")
//    public List<S3ObjectSummary> listImages() {
//        return s3Service.listImages();
//    }
//
//    @DeleteMapping("/delete")
//    public void deleteImage(@RequestParam("s3Key") String s3Key) {
//        s3Service.deleteImage(s3Key);
//    }
//}