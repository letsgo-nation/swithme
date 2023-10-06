package com.example.swithme.S3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "." + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    //S3 버킷에 이미지 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)  // PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //로컬에 있는 이미지 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    //변환
    private Optional<File> convert(MultipartFile file) throws IOException {
        //변환시 파일명에 또다른 독립성을 주기 위해 날짜+시간도 추가로 넣어줌
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File convertFile = new File(System.getProperty("user.dir") + "/" + now + ".jpg"); // 파일 변환

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // S3 delete file
    public void fileDelete(String fileName) {
        log.info("file name : " + fileName); //url

        if (fileName != null && !fileName.isEmpty()) {
            log.info("File : " + fileName.substring(53));

            try {
                amazonS3Client.deleteObject(this.bucket, fileName.substring(53));

            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
            }
        } else {
            log.error("fileName is null, cannot perform delete operation");
            // 파일 이름이 null인 경우에 대한 예외 처리 또는 오류 처리를 수행할 수 있음
        }
    }
}