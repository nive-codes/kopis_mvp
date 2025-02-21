package com.urbmi.mvp.common.file.service.impl;

import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import com.urbmi.mvp.common.file.dto.ComFileTempDeleteRequest;
import com.urbmi.mvp.common.file.dto.ComFileTempDomainRequest;
import com.urbmi.mvp.common.file.service.ComFileTempMetaService;
import com.urbmi.mvp.common.file.service.ComFileTempRestService;
import com.urbmi.mvp.common.file.service.ComFileType;
import com.urbmi.mvp.common.file.service.ComFileUploadService;
import com.urbmi.mvp.config.exception.business.BusinessException;
import com.urbmi.mvp.config.response.ApiCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

/**
 * @author nive
 * @class ComFIleTempRestServiceImpl
 * @desc ComFileTempRestService로 임시 파일 데이터 수정 및 변경 - ComFileTempMetaService와 ComFileUploadService 구현체
 * @since 2025-01-23
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ComFileTempRestServiceImpl implements ComFileTempRestService {

    private final ComFileTempMetaService comFileTempMetaService;
    private final ComFileUploadService comFileUploadService;


    @Override
    public ComFileTempDomain uploadFileTemp(MultipartFile[] files, ComFileTempDomainRequest comFileTempDomainRequest) {

        /*upload 처리 전 파일 request 검증*/
        validateRequest(files,comFileTempDomainRequest);

        String fileId = comFileTempDomainRequest.getFileId();
        ComFileTempDomain comFileTempDomain = new ComFileTempDomain();
        //   파일 반복문 처리
        for (MultipartFile file : files) {

            //   파일 검증 완료 후
            /*TODO 잘못된 파일인 경우 에러메세지 세분화 처리 - ex) document인데 img파일 업로드 했는 경우 세분화된 메세지 안내*/
            if(validateFile(file,comFileTempDomainRequest.getFileType(), comFileTempDomainRequest.getFileSize())){

                //   comFileTempDomain  생성
                comFileTempDomain = createFileTempDomain(file, comFileTempDomainRequest);

                //   실제로 파일 업로드
                if (comFileUploadService.uploadFile(file, comFileTempDomain.getFilePath(),comFileTempDomain.getFileUpldNm())) {

                    //파일 데이터 저장
                    fileId = processFileId(comFileTempDomain);
                    comFileTempDomain.setFileId(fileId);

                } else {
                    log.error("물리적 파일 업로드 실패 : {}", file.getOriginalFilename());
                    throw new BusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.",ApiCode.INTERNAL_SERVER_ERROR);
                }
            } else {
                log.error("물리적 파일 데이터 검증 실패 : {}", file.getOriginalFilename());
                throw new BusinessException("파일 업로드가 정상적으로 처리되지 못했습니다.",ApiCode.INTERNAL_SERVER_ERROR);
            }
        }
        return comFileTempDomain;
    }

    @Override
    public void deleteFileTemp(String fileId, ComFileTempDeleteRequest comFileTempDeleteRequest) {
        /*file_id 와 file_seq를 가지고 파일 임시 테이블 조회 경로 확인 -> 파일 삭제 -> 데이터 삭제*/

        if(Objects.isNull(comFileTempDeleteRequest.getFileId()) || comFileTempDeleteRequest.getFileId().isBlank()){
            log.error("파일 삭제 : fileId : {} 요청 - comFileTempDomain file 데이터가 없습니다.", fileId);
            throw new BusinessException("파일 삭제 fileId : "+ fileId +"는 요청되었으나 comFileTempDomain의 fileId가 존재하지 않습니다.",ApiCode.VALIDATION_FAILED);
        }

        if(!fileId.equals(comFileTempDeleteRequest.getFileId())){
            log.error("파일 삭제 : fileId : {} 요청과 comFileTempDomain fileId : {} 데이터가 일치 하지 않습니다.", fileId, comFileTempDeleteRequest.getFileId());
            throw new BusinessException("파일 삭제 fileId : "+ fileId +"는 요청되었으나 comFileTempDomain의 fileId가 존재하지 않습니다.",ApiCode.VALIDATION_FAILED);
        }


        //   file_id와 file_seq에 해당하는 데이터 조회
        ComFileTempDomain comFileTempDomain = ComFileTempDomain.builder().fileId(fileId).fileSeq(comFileTempDeleteRequest.getFileSeq()).build();
        ComFileTempDomain result = comFileTempMetaService.selectFileTempMeta(comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());

        if(result == null){
            log.error("TEMP 테이블 데이터 없음. file_id: {}, file_seq: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());

            throw new BusinessException("TEMP 테이블의 데이터를 찾을 수 없습니다", ApiCode.NOT_FOUND);
        }

        String fullFilePath = result.getFilePath() + "/" + result.getFileUpldNm();

        //   파일 존재 확인
        boolean isExist = comFileUploadService.isFileExist(fullFilePath);

        if(!isExist){
            log.error("실제 파일이 없음. file_id: {}, file_seq: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());
            throw new BusinessException("TEMP 테이블의 데이터의 파일이 실제로 없습니다.",ApiCode.VALIDATION_FAILED);
        }
        //   파일 삭제
        boolean deleted = comFileUploadService.deleteFile(fullFilePath);

        //   삭제 여부 확인
        if(!deleted){
            log.error("실제파일을 삭제할 수 없음. file_id: {}, file_seq: {}", comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());
            throw new BusinessException("존재하는 파일 삭제하지 못했습니다.",ApiCode.VALIDATION_FAILED);
        }

        comFileTempMetaService.deleteFileTempMeta(result);
        log.info("TEMP 메타 데이터 및 파일 삭제 성공(DELETE). file_id: {}, file_seq: {}",
                comFileTempDomain.getFileId(), comFileTempDomain.getFileSeq());

    }

    @Override
    public void deleteExpiredTemp(ComFileTempDomain comFileTempDomain) {

    }

    @Override
    public void deleteInvalidTemp(ComFileTempDomain comFileTempDomain) {

    }

    @Override
    public void deleteCompletedTemp(ComFileTempDomain comFileTempDomain) {

    }


    /**
     * 파일 upload 전 사전 검증 처리
     * @param files
     * @param comFileTempDomainRequest
     */
    private void validateRequest(MultipartFile[] files, ComFileTempDomainRequest comFileTempDomainRequest) {
        if (files == null || files.length == 0) {
            log.error("업로드할 파일이 없습니다.");
            throw new BusinessException("존재하지 않는 파일입니다.","NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        if(comFileTempDomainRequest == null){
            log.error("업로드 요청된 comFileTempDomainRequest가 존재하지 않습니다.");
            throw new BusinessException("파일 데이터 comFileTempDomain이 없습니다.","NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 파일 검증 로직을 공통 메소드로 분리
     * 1. 확장자 존재 여부 체크
     * 2. 확장자 type 체크 여부
     * @param file
     * @param fileType
     * @return boolean
     */
    private boolean validateFile(MultipartFile file, String fileType, long maxSizeMB) {
        log.info("파일 업로드 검증 합니다.");

//        if (file.isEmpty()) {
//            log.warn("업로드할 파일이 비어 있습니다.");
//            return false;
//        }

        // 확장자 존재여부 확인
        String fileSuffix = getFileSuffix(file);
        if (fileSuffix == null) {
            log.warn("파일 확장자가 없습니다. : {}", file.getOriginalFilename());
            return false;
        }

        // 확장자 검증
        if (!isExtValid(fileSuffix, fileType)) {
            log.warn("허용되지 않은 파일 확장자입니다. 파일: {}", file.getOriginalFilename());
            return false;
        }

        // 파일 용량 검증 확인
        if (!isFileSizeValid(file, maxSizeMB)){
            log.warn("파일 용량이 너무 큽니다. 파일: {}", file.getOriginalFilename());
            return false;
        }

        return true;
    }

    /**
     * 파일 확장자 return 메서드
     * @param file
     * @return
     */
    private String getFileSuffix(MultipartFile file) {
        // 파일 이름에서 확장자를 추출
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            // 파일 확장자 추출 (점(.)을 기준으로 분리)
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return null;  // 확장자가 없을 경우 null 반환
    }

    /**
     *
     * @param ext 파일 타입(image, txt 등)
     * @param fileType 첨부파일의 파일 확장자검증 Enum
     * @return
     */
    private boolean isExtValid(String ext, String fileType) {
        /*TODO String의 파일 type으로 ComFileType enum 조회 */

        if(ext == null || ext.isBlank()){
            return false;
        }else{
            // 확장자에서 "."을 제거하고 소문자로 변환
            String cleanExt = ext.trim().toLowerCase().replace(".", "");

            // 파일 타입(Enum)을 가져옴
            ComFileType type = ComFileType.getFileTypeByExtension(cleanExt);

            // 파일 타입이 null이 아니면, 해당 타입에 확장자가 포함되어 있는지 확인
            return type != null && type.name().equalsIgnoreCase(fileType);

        }
    }

    /***
     * 파일 용량 검증 메서드
     * @param file
     * @param maxSizeMB
     * @return
     */
    private boolean isFileSizeValid(MultipartFile file, long maxSizeMB) {
        long fileSizeMB = file.getSize() / (1024 * 1024);  // MB 단위로 변환
        return fileSizeMB <= maxSizeMB;  // maxSizeMB보다 작거나 같으면 유효
    }


    /**
     * 이미지파일 확인 메서드 후 thumnail 생성 하도록 처리(각 모듈 단에서 호출 및 썸네일 생성 처리
     * @param file
     * @return boolean
     */
    private boolean isImage(MultipartFile file) {
        String suffix = getFileSuffix(file);
        if (suffix != null) {
            // 이미지 파일 확장자 목록
            return suffix.equalsIgnoreCase(".jpg") ||
                    suffix.equalsIgnoreCase(".jpeg") ||
                    suffix.equalsIgnoreCase(".png") ||
                    suffix.equalsIgnoreCase(".gif") ||
                    suffix.equalsIgnoreCase(".bmp") ||
                    suffix.equalsIgnoreCase(".tiff");
        }
        return false;  // 이미지 확장자가 아닌 경우
    }


    /**
     * 변수로 받아온 파라미터를 토대로 ComFileTempDomain 생성
     * @param file
     * @param comFileTempDomainRequest
     * @return
     */
    private ComFileTempDomain createFileTempDomain(MultipartFile file,ComFileTempDomainRequest comFileTempDomainRequest) {
//        ComFileDomain comFileDomain = new ComFileDomain();
        ComFileTempDomain comFileTempDomain = new ComFileTempDomain();
        comFileTempDomain.setFileId(comFileTempDomainRequest.getFileId());
        comFileTempDomain.setFilePath(comFileTempDomainRequest.getFilePath());    //파일 경로(모듈 명)
        comFileTempDomain.setFileModule(comFileTempDomainRequest.getFilePath());    //파일 경로(모듈 명)
        comFileTempDomain.setFileOrignNm(file.getOriginalFilename());   //실제 파일 업로드명
        comFileTempDomain.setFileSize(file.getSize()); //파일 사이즈
        comFileTempDomain.setFileUpldNm(UUID.randomUUID().toString() + getFileSuffix(file));    //고유한 파일name 값 처리
        return comFileTempDomain;
    }


    /**
     * FileMetaService의 생성 / 수정 시 fileId를 반환 (comFileDomain의 fileID가 있는지 검증 후 fileId 재생성 여부 체크
     * @param comFileTempDomain
     * @param comFileTempDomain
     * @return String
     */
    private String processFileId(ComFileTempDomain comFileTempDomain) {
        String fileId = comFileTempDomain.getFileId();

        // fileId가 null 또는 빈 값인 경우 새로 추가, 아니면 업데이트
        try {
            if (fileId == null || fileId.isBlank()) {
                fileId = comFileTempMetaService.insertFileTempMeta(comFileTempDomain);
            } else {
                comFileTempMetaService.updateFileTempMeta(comFileTempDomain);
            }
        } catch (Exception e) {
            log.error("파일 메타 데이터 처리 실패 {}", e.getMessage());
            throw new BusinessException("파일 메타 데이터 처리 실패", ApiCode.VALIDATION_FAILED);
        }

        return fileId;
    }
}
