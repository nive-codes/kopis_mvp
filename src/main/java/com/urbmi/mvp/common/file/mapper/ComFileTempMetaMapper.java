package com.urbmi.mvp.common.file.mapper;

import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComFileTempMetaMapper {

    /*Temp meta insert*/
    void insertFileTempMeta(ComFileTempDomain comFileTempDomain);


    /*Temp meta update status*/
    void updateFileTempMetaStatus(ComFileTempDomain comFileTempDomain);

    void deleteFileTempMeta(ComFileTempDomain comFileTempDomain);

    ComFileTempDomain selectLatestFileTempMeta(String fileId);

    ComFileTempDomain selectFileTempMeta(String fileId, int fileSeq);

    String selectLatestFileTempMetaPath(String fileId);

    String selectFileMetaTempPath(String fileId, String fileSeq);

    List<ComFileTempDomain> selectFileTempMetaList(String fileId);

    int selectFileTempMetaListCount(String fileId);


}
