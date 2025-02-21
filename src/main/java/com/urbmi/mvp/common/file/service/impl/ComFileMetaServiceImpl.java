package com.urbmi.mvp.common.file.service.impl;

import com.urbmi.mvp.common.file.domain.ComFileDomain;
import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import com.urbmi.mvp.common.file.mapper.ComFileMetaMapper;
import com.urbmi.mvp.common.file.service.ComFileMetaService;
import com.urbmi.mvp.common.idgen.ComTableIdGnrService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author nive
 * @class ComFileMetaServiceImpl
 * @desc 파일업로드 공통 서비스 - 파일 정보(meta)를 DB에서 관리하는 service의 구현체
 * @since 2025-01-16
 */
@Service
@AllArgsConstructor
@Transactional
public class ComFileMetaServiceImpl implements ComFileMetaService {

    private final ComFileMetaMapper comFileMetaMapper;
    private final ComTableIdGnrService fileIdGenService;

    @Override
    public String insertFileMeta(ComFileTempDomain comFileTempDomain) {
        ComFileDomain comFileDomain = ComFileDomain.builder()
                .fileId(comFileTempDomain.getFileId())
                .filePath(comFileTempDomain.getFilePath())
                .fileSize(comFileTempDomain.getFileSize())
                .fileOrd(comFileTempDomain.getFileOrd())
                .fileUpldNm(comFileTempDomain.getFileUpldNm())
                .fileOrignNm(comFileTempDomain.getFileOrignNm())
                .fileModule(comFileTempDomain.getFileModule())
                .build();

        comFileMetaMapper.insertFileMeta(comFileDomain);
        return comFileDomain.getFileId();
    }


    /*TODO 사용안함--삭제예정*/
    @Override
    public String insertFileMeta(ComFileDomain comFileDomain) {
        String id = fileIdGenService.getNextId();
        comFileDomain.setFileId(id);
        comFileMetaMapper.insertFileMeta(comFileDomain);
        return id;
    }


    @Override
    public void updateFileMeta(ComFileDomain comFileDomain) {
        /*기존 fileId로 update처리*/
        comFileMetaMapper.insertFileMeta(comFileDomain);

    }

    @Override
    public ComFileDomain selectLatestFileMeta(String fileId) {
        return comFileMetaMapper.selectLatestFileMeta(fileId);
    }

    @Override
    public ComFileDomain selectFileMeta(String fileId, int fileSeq) {
        return comFileMetaMapper.selectFileMeta(fileId,fileSeq);
    }

    @Override
    public String selectLatestFileMetaPath(String fileId) {
        return comFileMetaMapper.selectLatestFileMetaPath(fileId);
    }

    @Override
    public String selectFileMetaPath(String fileId, int fileSeq) {
        return comFileMetaMapper.selectFileMetaPath(fileId,fileSeq);
    }

    @Override
    public List<ComFileDomain> selectFileMetaList(String fileId) {
        return comFileMetaMapper.selectFileMetaList(fileId);
    }

    @Override
    public int selectFileMetaListCount(String fileId) {
        return comFileMetaMapper.selectFileMetaListCount(fileId);
    }

    @Override
    public void deleteFileMetaList(String fileId) {
        comFileMetaMapper.deleteFileMetaList(fileId);
    }

    @Override
    public void deleteFileMeta(String fileId, int fileSeq) {
        comFileMetaMapper.deleteFileMeta(fileId,fileSeq);
    }
}
