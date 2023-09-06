//package com.example.swithme.aws;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration // 스프링 부트에 Configuration임을 알려줌
//// S3와 스프링부트를 연결하는 개체 생성 코드를 만드는 곳
//public class S3Config {
//
//    // @Valid로 application.properties에 있는 변수 가져오기
//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secretKey;
//
//    @Value("${cloud.aws.region.static}")
//    private String region;
//
//    // S3에 접근할 수 있도록 빈 등록
//    @Bean
//    public AmazonS3Client amazonS3Client() {
//
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//
//        return (AmazonS3Client) AmazonS3ClientBuilder
//                .standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .build();
//
//    }
//}
