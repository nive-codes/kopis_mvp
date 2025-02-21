package com.urbmi.mvp.common.file.service;

import com.urbmi.mvp.common.file.domain.ComFileTempDomain;

import java.util.List;

/**
 * @author nive
 * @class ComFileTempMetaService
 * @desc 파일 임시 테이블을 관리하는 service
 * @since 2025-01-23
 */
public interface ComFileTempMetaService {

    /**
     * 파일 TEMP 데이터 insert + 파일 업로드 (PK생성)
     * @param comFileTempDomain
     * @return String
     */
    String insertFileTempMeta(ComFileTempDomain comFileTempDomain);

    /**
     * 파일 TEMP 데이터 insert + 파일 업로드
     * @param comFileTempDomain
     * @return
     */
    void updateFileTempMeta(ComFileTempDomain comFileTempDomain);

    /**
     * Com_File로 넘어간 데이터, 못넘어간 데이터 status 처리(fileId,fileSeq)
     * @param comFileTempDomain
     */
    void updateFileTempStatus(ComFileTempDomain comFileTempDomain);

    /**
     * 사용자 요청 삭제
     * @param comFileTempDomain
     */
    void deleteFileTempMeta(ComFileTempDomain comFileTempDomain);

    ComFileTempDomain selectFileTempMeta(String fileId, int fileSeq);

    List<ComFileTempDomain> selectFileTempMeta(String fileId);




}
