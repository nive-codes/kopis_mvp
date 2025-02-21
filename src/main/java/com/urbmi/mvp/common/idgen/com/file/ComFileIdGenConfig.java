package com.urbmi.mvp.common.idgen.com.file;

import com.urbmi.mvp.common.idgen.ComTableIdGnrService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author nive
 * @class ComFileIdGenConfig
 * @desc ComFileMetaService의 idgen 설정
 * @since 2025-01-16
 */
@Configuration
public class ComFileIdGenConfig {

    // MyBatisConfig에서 정의한 DataSource를 주입받음
    private final DataSource dataSource;

    public ComFileIdGenConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ComTableIdGnrService fileIdGenService() {
        ComTableIdGnrService idGenService = new ComTableIdGnrService();
        idGenService.setDataSource(dataSource);  // 공통 DataSource 사용
        idGenService.setBlockSize(10);             // file 모듈에 맞는 blockSize 설정
        idGenService.setStrategy("FILE_");  // file 모듈에 맞는 전략 설정
        idGenService.setTableName("COM_FILE");   // file 모듈에 맞는 테이블명 설정

        return idGenService;
    }
}
