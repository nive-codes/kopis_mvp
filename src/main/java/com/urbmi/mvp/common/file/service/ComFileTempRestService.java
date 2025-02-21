package com.urbmi.mvp.common.file.service;

import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import com.urbmi.mvp.common.file.dto.ComFileTempDeleteRequest;
import com.urbmi.mvp.common.file.dto.ComFileTempDomainRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nive
 * @class ComFileTempRestService
 * @desc ComFileTempRestService로 임시 파일 데이터 수정 및 변경 - ComFileTempMetaService와 ComFileUploadService 조합
 * @since 2025-01-23
 */
public interface ComFileTempRestService {
    /**
     * 임시 테이블 저장 및 파일 업로드(parameter로 받은 fileType으로 데이터 검증, 용량 검증, 파일 확장자 체크)
     * @param files 업로드한 파일
     * @Param comFileTempDomain COmFileTempDomain 객체
     */
//    String uploadFileTemp(MultipartFile[] files, ComFileTempDomain comFileTempDomain);
    ComFileTempDomain uploadFileTemp(MultipartFile[] files, ComFileTempDomainRequest comFileTempDomainRequest);

    /** 사용자가 삭제 버튼
     * 임시 테이블의 데이터 삭제 및 실제 파일 삭제(임시 파일까지 삭제처리를 한다)
     * @param comFileTempDeleteRequest
     */
    void deleteFileTemp(String fileId, ComFileTempDeleteRequest comFileTempDeleteRequest);




    /** TODO
     * cron 혹은 Spring batch 활용
     * pending 데이터 expire_dt 확인 후 invalid 상태 전환(invalid_dt update)
     * @param comFileTempDomain
     */
    void deleteExpiredTemp(ComFileTempDomain comFileTempDomain);

    /** TODO
     * cron 혹은 Spring batch 활용
     * invalid 데이터 invalid_dt 확인 후 물리 삭제(파일도 함께 삭제)
     * @param comFileTempDomain
     */
    void deleteInvalidTemp(ComFileTempDomain comFileTempDomain);

    /** TODO
     * cron 혹은 Spring batch 활용
     * valid 정상적으로 이관된 테이블의 데이터 삭제
     * @param comFileTempDomain
     */
    void deleteCompletedTemp(ComFileTempDomain comFileTempDomain);


}
