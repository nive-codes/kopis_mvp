package com.urbmi.mvp.common.file.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbmi.mvp.common.file.domain.ComFileDomain;
import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import com.urbmi.mvp.common.file.dto.ComFileDeleteRequest;
import com.urbmi.mvp.common.file.service.*;
import com.urbmi.mvp.config.exception.business.BusinessException;
import com.urbmi.mvp.config.response.ApiCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author nive
 * @class ComFileRestService
 * @since 2025-01-23
 * @desc ComFileRestService를 통한 파일 temp 데이터 검증, 파일 검증, metaService 및 uploadService 핸들링
 * 각 모듈에서의 insert 요청 발생 시 TEMP 파일 검증
 * 각 모듈에서의 update 요청 발생 시 TEMP 파일 검증
 * 각 모듈에서의 파일 delete요청 시 파일 테이블 논리 삭제(spring batch를 활용한 물리 삭제 예정)
 * 각 모듈 데이터 delete 요청 시 파일 테이블 논리 삭제(spring batch를 활용한 물리 삭제 예정)
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ComFileRestServiceImpl implements  ComFileRestService {

    private final ComFileMetaService comFileMetaService;
    private final ComFileTempMetaService comFileTempMetaService;
    private final ComFileUploadService comFileUploadService;

    //   성능 최적화 (싱글톤 유지 성능 최적화 + 쓰레드 세이프이기 때문에 여러 요청에서 공유
    //   매번 객체 생성보다 재사용이 효율적
    //   JSON 변환이 자주 일어나는 서비스라면 재사용이 좋음
    //   큰 리소스를 잡아먹지 않음
    //   한번 생성 후 계속 사용하는 것이 좋음.
    //   TODO 위와 같은 이유라면 Spring Bean으로 등록 후 사용을 염두할 것.
    private static final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 재사용

    @Override
    public void transferFile(String fileId,ComFileType comFileType, Long fileSize, int fileCnt, String filePath) {
        // 파일ID에 해당하는 PENDING 상태의 파일 목록 조회
        List<ComFileTempDomain> comFileTempDomainList = comFileTempMetaService.selectFileTempMeta(fileId);
        int allFileSize = comFileTempDomainList.size();

        // 전체 개수 검증
        if (!isCntValid(allFileSize, fileCnt)) {
            log.error("TEMP 파일 {} 개수 지정 초과: TEMP 저장 수 {}, 허용 개수 {}", fileId, allFileSize, fileCnt);

            /*PENDING 상태로 그대로 처리-알아서 삭제 하도록*/
            throw new BusinessException("파일 저장 개수가 잘못되었습니다.", ApiCode.VALIDATION_FAILED);
        }

        // 파일 목록 검증 및 이전 처리
        Iterator<ComFileTempDomain> iterator = comFileTempDomainList.iterator();
        while (iterator.hasNext()) {
            ComFileTempDomain comFileTempDomain = iterator.next();

            // 경로 검증
            if (!isPathValid(comFileTempDomain, filePath)) {
                log.warn("잘못된 파일 경로가 유효하지 않습니다. fileId: {}, fileSeq: {}, filePath: {}",comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), filePath);

                comFileTempDomain.setInvalidReason("잘못된 파일 경로로 유효하지 않습니다.");
                updateFileStatusInvalid(comFileTempDomain);
                throw new BusinessException("유효하지 않는 파일 경로로 저장되려고 합니다. 잘못된 파일을 삭제 후 재진행 해주세요.", ApiCode.VALIDATION_FAILED,ComFileTempDomain.builder().fileSeq(comFileTempDomain.getFileSeq()).fileId(fileId).build());

            }
            // 사이즈 검증
            else if (!isSizeVaild(comFileTempDomain, fileSize)) {
                log.warn("파일 크기가 초과되었습니다. fileId: {}, fileSeq: {}, 제한 크기: {} bytes, 현재 크기: {} bytes",
                        comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), fileSize, comFileTempDomain.getFileSize());

                comFileTempDomain.setInvalidReason("파일 크기가 초과되었습니다.");
                updateFileStatusInvalid(comFileTempDomain);
                throw new BusinessException("첨부파일의 크기가 잘못되었습니다.", ApiCode.VALIDATION_FAILED,ComFileTempDomain.builder().fileSeq(comFileTempDomain.getFileSeq()).fileId(fileId).build());

            }
            // 확장자 검증
            else if (!isExtValid(comFileTempDomain, comFileType)) {
                log.warn("파일 확장자가 유효하지 않습니다. fileId: {}, fileSeq: {}, fileName: {}",
                        comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), comFileTempDomain.getFileUpldNm());

                comFileTempDomain.setInvalidReason("파일 확장자가 유효하지 않습니다.");
                updateFileStatusInvalid(comFileTempDomain);

                throw new BusinessException("첨부파일 확장자가 잘못되었습니다.", ApiCode.VALIDATION_FAILED,ComFileTempDomain.builder().fileSeq(comFileTempDomain.getFileSeq()).fileId(fileId).build());
            }

            // 파일 상태 업데이트
            comFileTempDomain.setFileStatus("VALID");
            comFileTempMetaService.updateFileTempStatus(comFileTempDomain);

            // 파일 메타 정보 본 테이블로 이전
            comFileMetaService.insertFileMeta(comFileTempDomain);

        }

        log.info("파일업로드 종료 : TEMP 파일 {} 개수 {} 중 {} 개 업로드 완료", fileId, allFileSize, comFileTempDomainList.size());
    }

    @Override
    public void deleteFile(String fileId, ComFileDeleteRequest comFileDeleteRequest) {

        if(Objects.isNull(comFileDeleteRequest.getFileId()) || comFileDeleteRequest.getFileId().isBlank()){
            log.error("파일 삭제 : fileId : {} 요청 - comFileDeleteRequest file 데이터가 없습니다.", fileId);
            throw new BusinessException("파일 삭제 fileId : "+ fileId +"는 요청되었으나 comFileTempDomain의 fileId가 존재하지 않습니다.",ApiCode.VALIDATION_FAILED);
        }

        if(!fileId.equals(comFileDeleteRequest.getFileId())){
            log.error("파일 삭제 : fileId : {} 요청과 comFileDeleteRequest fileId : {} 데이터가 일치 하지 않습니다.", fileId, comFileDeleteRequest.getFileId());
            throw new BusinessException("파일 삭제 fileId : "+ fileId +"는 요청되었으나 comFileTempDomain의 fileId가 존재하지 않습니다.",ApiCode.VALIDATION_FAILED);
        }
        ComFileDomain result = comFileMetaService.selectFileMeta(comFileDeleteRequest.getFileId(), comFileDeleteRequest.getFileSeq());
        if(result == null){
            log.error("COM_FILE 테이블 데이터 없음. file_id: {}, file_seq: {}", comFileDeleteRequest.getFileId(), comFileDeleteRequest.getFileSeq());

            throw new BusinessException("COM_FILE 테이블의 데이터를 찾을 수 없습니다", ApiCode.NOT_FOUND);
        }

        /*업로드가 완료된 파일의 경우 메타 데이터만 논리 삭제 이후 추후 배치로 전체 파일 삭제*/
        comFileMetaService.deleteFileMeta(comFileDeleteRequest.getFileId(), comFileDeleteRequest.getFileSeq());
        log.info("TEMP 메타 데이터 및 파일 삭제 성공(DELETE). file_id: {}, file_seq: {}",
                comFileDeleteRequest.getFileId(), comFileDeleteRequest.getFileSeq());
    }

    @Override
    public void deleteExpired(ComFileTempDomain comFileTempDomain) {
        //   TODO 논리 삭제된 파일 데이터 delete 처리 및 실제 파일 삭제 처리

    }

    @Override
    public String selectFileMetaJsonList(String fileId) {
//        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writeValueAsString(comFileMetaService.selectFileMetaList(fileId));
        }catch (JsonProcessingException e){
            log.error("파일 목록 JSON 변환 에러 발생 JsonProcessingException ",e);

            throw new BusinessException("파일 목록 JSON 변환 중 에러가 발생했습니다.",ApiCode.VALIDATION_FAILED);
        }catch (Exception e){
            log.error("파일 목록 JSON 변환 에러 발생 Exception ",e);
            throw new BusinessException("파일 목록을 조회하는 중 오류가 발생했습니다.", ApiCode.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public String selectFileMetaJson(String fileId, int fileSeq) {
//        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writeValueAsString(comFileMetaService.selectFileMeta(fileId, fileSeq));
        }catch (JsonProcessingException e){
            log.error("단일 파일 JSON 변환 에러 발생 JsonProcessingException ",e);
            throw new BusinessException("파일 목록 JSON 변환 중 에러가 발생했습니다.",ApiCode.VALIDATION_FAILED);
        }catch (Exception e){
            log.error("단일 파일 JSON 변환 에러 발생 Exception",e);
            throw new BusinessException("단일 파일 정보를 조회하는 중 오류가 발생했습니다.", ApiCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ComFileDomain> selectFileMetaList(String fileId) {

        return comFileMetaService.selectFileMetaList(fileId);
    }

    @Override
    public ComFileDomain selectFileMeta(String fileId, int fileSeq) {
        return comFileMetaService.selectFileMeta(fileId, fileSeq);
    }

    @Override
    public ComFileDomain fileBinaryStream(String fileId, Integer fileSeq) {
        ComFileDomain fileDomain = comFileMetaService.selectFileMeta(fileId, fileSeq);

        if(fileDomain == null){
            log.error("잘못된 파일 stream 요청 : {}, file Seq : {}",fileId,fileSeq);
            throw new BusinessException("파일 정보를 찾을 수 없습니다. 잘못된 파일 파라미터 입니다.");
        }

//        File selectFile = comFileUploadService.selectFile(fileDomain.getFilePath() + "/" + fileDomain.getFileUpldNm());

        Resource fileResource = comFileUploadService.selectFileStream(fileDomain.getFilePath() + "/" + fileDomain.getFileUpldNm());
        if(fileDomain == null){
            log.error("저장되어 있는 파일이 없습니다 : {}, file Seq : {}",fileId,fileSeq);
            throw new BusinessException("저장된 파일이 없습니다.");
        }

        fileDomain.setMediaType(MediaTypeFactory.getMediaType(fileResource).orElse(MediaType.APPLICATION_OCTET_STREAM));
        fileDomain.setFileResource(fileResource);
        return fileDomain;
    }


    //   용량 검증
    private boolean isSizeVaild(ComFileTempDomain comFileTempDomain,Long maxSizeMB){
        long fileSizeMB = comFileTempDomain.getFileSize() / (1024 * 1024);  // MB 단위로 변환
        return fileSizeMB <= maxSizeMB;

    }

    //   파일 개수 검증
    private boolean isCntValid(int comFileTempDomainSize, int fileCnt){
        return comFileTempDomainSize <= fileCnt;
    }

    //   파일 저장 경로 검증
    private boolean isPathValid(ComFileTempDomain comFileTempDomain, String filePath){
        return filePath.equals(comFileTempDomain.getFilePath());
//        if(!filePath.equals(comFileTempDomain.getFilePath())){
//            return false;
//        }else{
//            return true;
//        }
    }

    //   파일 확장자 검증(파일타입)
    private boolean isExtValid(ComFileTempDomain comFileTempDomain, ComFileType comFileType){
        String fileSuffix = getFileSuffix(comFileTempDomain.getFileUpldNm());

        if (fileSuffix.isEmpty()) {
            log.warn("파일 명이 잘못되었거나 확장자가 없습니다. fileId: {}, fileSeq: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());
            return false;
        }

        ComFileType fileType = ComFileType.getFileTypeByExtension(fileSuffix);

        if (fileType == null) {
            log.warn("지원되지 않는 파일 확장자입니다. fileId: {}, fileSeq: {}, 확장자: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq(), fileSuffix);
            return false;
        }

        return fileType.equals(comFileType);
    }

    /**
     * 파일 확장자 반환 메서드
     * @param fileNm 파일명
     * @return 확장자 (소문자로 변환), 없으면 빈 문자열 반환
     */
    private String getFileSuffix(String fileNm) {
        if (Objects.isNull(fileNm) || fileNm.isBlank() || !fileNm.contains(".")) {
            return "";
        }
        return fileNm.substring(fileNm.lastIndexOf(".") + 1).toLowerCase();
    }


    /*invalid update는 commit 처리*/
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateFileStatusInvalid(ComFileTempDomain comFileTempDomain) {
        comFileTempDomain.setFileStatus("INVALID");
        comFileTempMetaService.updateFileTempStatus(comFileTempDomain);
    }


}
