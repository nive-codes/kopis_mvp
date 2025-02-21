package com.urbmi.mvp.common.file.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author nive
 * @class ComFileTempDomain
 * @desc 임시 파일 저장 관리 DOMAIN
 * @since 2025-01-23
 */
@Schema(description = "00. 임시 파일 도메인 정보")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComFileTempDomain {

    @Schema(description = "임시 파일 고유 ID (PK)", example = "1")
    private Long tempId;

    @Schema(description = "파일 고유 ID", example = "FILE_123456")
    private String fileId;

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
    @Schema(description = "파일 정렬 순서", example = "0")
    private Integer fileOrd = 0;

    @Schema(description = "파일 상태 (PENDING: 업로드 대기, VALID: 유효, INVALID: 검증 실패)", example = "PENDING")
    private String fileStatus;

//    @Schema(description = "파일 제한을 사용할 타입(IMAGE,DOCUMENT,AUDIO,VIDEO) - ComFileType 참조", example = "IMAGE")
//    private String fileType;

    @Schema(description = "파일 만료 시간 (PENDING에서의 지정된 시간 이후 INVALID 됨)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDt;

    @Schema(description = "파일이 최종적으로 이관된 시간")
    private Date transferDt;

    @Schema(description = "파일이 INVALID로 처리된 시간(INAVLID 이후 물리 삭제 됨)")
    private Date invalidDt;

    @Schema(description = "파일이 INVALID로 처리된 이유", example = "지원하지 않는 확장자 형식")
    private String invalidReason;

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
}
