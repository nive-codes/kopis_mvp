package com.urbmi.mvp.common.file.service.impl;

import com.urbmi.mvp.common.file.service.ComFileUploadService;
import com.urbmi.mvp.config.exception.business.BusinessException;
import com.urbmi.mvp.config.response.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author nive
 * @class FileS3UploadService
 * @desc AWS S3의 파일 업로드 서비스 impl
 * config/FileConfig.java 및 yml의 설정에 따라 bean 생성
 * FileConfig에서 yml의 bucket name을 받도록 수정
 * @since 2025-01-16
 */

@Slf4j
public class ComFileS3UploadService implements ComFileUploadService {

    private final S3Client s3Client;
    private final String bucketName;

    public ComFileS3UploadService(S3Client s3Client,String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public boolean uploadFile(MultipartFile file, String filePath, String fileName) {

        log.warn("s3Client.uploadFile bucket name {}" +bucketName);


        try {
            //  업로드할 파일을 RequestBody로 변환
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath+"/"+fileName) // S3 내 파일 경로
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("File uploaded to S3 successfully: {}", filePath+"/"+fileName);
            return true;
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            return false;
        }
    }

    /*사실 경로만 전달하면 될 것으로 예상됨... 파일을 다시 가지고 올 필요는 없지 않을지 검토*/
    @Override
    public File selectFile(String filePath) {
        try {
            // 로컬에 파일 다운로드
            File tempFile = File.createTempFile("s3-", filePath.replace("/", "_"));
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            s3Client.getObject(getObjectRequest, Path.of(tempFile.getPath()));
            log.info("File downloaded from S3: {}", filePath);
            return tempFile;
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류가 발생했습니다. filePath :  {}", filePath, e);
            return null;
        }
    }

    @Override
    public Resource selectFileStream(String filePath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            // S3에서 파일을 직접 스트리밍
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            log.info("File streamed from S3: {}", filePath);
            return new InputStreamResource(s3Object); // InputStreamResource 반환
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류가 발생했습니다. filePath :  {}", filePath, e);
            return null;
        }
    }

    @Override
    public boolean deleteFile(String filePath) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("s3파일 삭제 경로: {}", filePath);
            return true;
        } catch (Exception e) {
            log.info("s3파일 삭제 경로 오류 발생: {}", filePath);
            log.error("S3 파일 삭제 중 오류가 발생했습니다. : {}", e);
            return false;
        }
    }

    @Override
    public boolean isFileExist(String filePath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            s3Client.getObject(getObjectRequest);
            return true; // 파일이 존재하면 예외 없이 처리됨
        } catch (Exception e) {
            log.error("File does not exist in S3: {}", filePath, e);
            throw new BusinessException("S3 파일 조회 중 오류가 발생했습니다.", ApiCode.VALIDATION_FAILED);
        }
    }

    @Override
    public File uploadThumbnail(MultipartFile file, String filePath) {
        return null;
    }
}
