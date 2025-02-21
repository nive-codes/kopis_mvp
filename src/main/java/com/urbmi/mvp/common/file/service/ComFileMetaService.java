package com.urbmi.mvp.common.file.service;

import com.urbmi.mvp.common.file.domain.ComFileDomain;
import com.urbmi.mvp.common.file.domain.ComFileTempDomain;

import java.util.List;

/**
 * @author nive
 * @class ComFileMetaService
 * @desc 파일업로드 공통 서비스 - 파일 정보(meta)를 DB에서 관리하는 service
 * 추상화의 구현체는 ComFileServicImple의 구현체에서 활용
 * ComFileTempDomain 객체 추가 및 temp 폴더에 파일 임시저장, 모듈에서 save 로직 동작 시 원래 파일로 insert되는 서비스 로직으로 변경(모듈 폼 전송 -> 비동기 파일 업로드로 아키텍처 변경 - 25.01.23)
 * TODO ord 바꾸는 쿼리 및 동작 추가예정
 * @since 2025-01-16
 */
public interface ComFileMetaService {


    /**
     * 비동기 요청 파일 데이터 전송
     * ComFileTempDomain -> ComFileDomain으로 이전 처리(COM_FILE_TEMP -> COM_FILE)
     * @param comFileTempDomain
     * @return String
     */
    String insertFileMeta(ComFileTempDomain comFileTempDomain);

    /**
     * form 데이터 전송 시(TODO 삭제 예정)
     * 파일 데이터 insert + 파일 업로드 (SEQ 갱신 값 insert)
     * 파일seq 증가
     * @param comFileDomain
     * @return void
     */
    String insertFileMeta(ComFileDomain comFileDomain);

    /**
     * 파일 데이터 upate + 파일 업로드 (SEQ 갱신 값 insert)
     * 파일seq 증가
     * @param comFileDomain
     * @return void
     */
    void updateFileMeta(ComFileDomain comFileDomain);

    /**
     * 단일 fileId와 seq에서의 select 및 파일 경로확인(file_seq 최신 순)
     * @param fileId
     * @return ComFileDomain
     */
    ComFileDomain selectLatestFileMeta(String fileId);

    /**
     * 단일 fileId와 seq에서의 select 및 파일 경로확인
     * @param fileId
     * @param fileSeq
     * @return ComFileDomain
     */
    ComFileDomain selectFileMeta(String fileId, int fileSeq);

    /**
     * 단일 파일 경로 확인(최신파일 seq desc)
     * @param fileId
     * @return String
     */
    String selectLatestFileMetaPath(String fileId);

    /**
     * 단일 파일 경로 확인
     * @param fileId
     * @param fileSeq
     * @return String
     */
    String selectFileMetaPath(String fileId, int fileSeq);


    /**
     * fileId의 전체 파일 목록
     * @param fileId
     * @return List<ComFileDomain>
     */
    List<ComFileDomain> selectFileMetaList(String fileId);

    /**
     * fileId의 전체 파일 목록 개수
     * @param fileId
     * @return List<ComFileDomain>
     */
    int selectFileMetaListCount(String fileId);



    /**
     * fileId에 해당하는 seq 전체 논리 삭제
     * @param fileId
     * @return void
     */
    void deleteFileMetaList(String fileId);

    /**
     * fileId + fileSeq에 해당하는 단일 파일 삭제
     * @param fileId
     * @param fileSeq
     * @return void
     */
    void deleteFileMeta(String fileId,int fileSeq);


}
