package com.urbmi.mvp.common.file.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.util.Date;

/**
 * @author nive
 * @class ComFileDomain
 * @desc 파일 업로드 domain 객체
 * @since 2025-01-16
 */
@Schema(description = "01. 본 파일 도메인 정보")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComFileDomain {

    @Schema(description = "파일 고유 ID", example = "FILE_123456")
    private String fileId;

    @Schema(description = "부모 파일 ID (버전 관리용, 없으면 NULL)", example = "FILE_123456")
    private String fileParentId;

    @Builder.Default
    @Schema(description = "파일 시퀀스 (동일한 파일 ID 내에서 순차적으로 증가)", example = "1")
    private Integer fileSeq = 0;

    @Schema(description = "업로드된 파일 이름", example = "uploaded_image.png")
    private String fileUpldNm;

    @Schema(description = "원본 파일 이름", example = "original_image.png")
    private String fileOrignNm;

    @Schema(description = "파일이 저장된 경로", example = "/uploads/2025/01/")
    private String filePath;

    @Schema(description = "파일 업로드를 요청한 모듈명 (예: 회원 테이블)", example = "memberTb")
    private String fileModule;

    @Builder.Default
    @Schema(description = "파일 크기 (Byte 단위)", example = "1048576")
    private long fileSize = 10L;

    @Builder.Default
    @Schema(description = "파일 정렬 순서 (DB에서 자동 업데이트됨)", example = "1")
    private Integer fileOrd = 0;

    @Schema(description = "파일 삭제 여부 (Y: 삭제, N: 사용 중)", example = "N")
    private String delYn;

    @Schema(description = "썸네일 여부 (Y: 썸네일, N: 원본)", example = "N")
    private String thumYn;

    @Schema(description = "파일 생성 일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtDt;

    @Schema(description = "파일 생성자 ID", example = "admin")
    private String crtId;

    @Schema(description = "파일 생성 시 IP 주소", example = "192.168.0.1")
    private String crtIpAddr;

    @Schema(description = "파일 수정 일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updDt;

    @Schema(description = "파일 수정자 ID", example = "user123")
    private String updId;

    @Schema(description = "파일 수정 시 IP 주소", example = "192.168.0.2")
    private String updIpAddr;

    @Schema(description = "파일 삭제 일시")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date delDt;

    @Schema(description = "파일 삭제자 ID", example = "admin")
    private String delId;

    @Schema(description = "파일 삭제 시 IP 주소", example = "192.168.0.3")
    private String delIpAddr;

    @Schema(description = "파일 stream용 fileResource")
    private Resource fileResource;

    @Schema(description = "파일 media 타입")
    private MediaType mediaType;


}
