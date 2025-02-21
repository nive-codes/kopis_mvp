package com.urbmi.mvp.config;

import com.urbmi.mvp.common.file.service.ComFileUploadService;
import com.urbmi.mvp.common.file.service.impl.ComFileLocalUploadService;
import com.urbmi.mvp.common.file.service.impl.ComFileS3UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author nive
 * @class FileConfig
 * @desc yml의 profile에 따른 FileUpload bean 수동 생성입니다.
 * @since 2025-01-16
 */
@Configuration
public class FileConfig {

    @Bean
//    @Profile("local") // local 프로파일에서만 사용
    public ComFileUploadService fileLocalUploadService() {
        return new ComFileLocalUploadService();
    }

//    @Bean
//    @Profile("s3") // s3 프로파일에서만 사용
//    public ComFileUploadService fileS3UploadService(S3Client s3Client, @Value("${spring.file-storage.bucket}") String bucketName) {
//        return new ComFileS3UploadService(s3Client, bucketName);
//    }
}


