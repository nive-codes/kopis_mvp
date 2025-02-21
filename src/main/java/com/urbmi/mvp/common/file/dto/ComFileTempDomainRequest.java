package com.urbmi.mvp.common.file.dto;

import com.urbmi.mvp.common.file.domain.ComFileTempDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nive
 * @class ComFileTempDomain
 * @desc 임시 파일 저장 관리 DOMAIN
 * @since 2025-01-23
 */
@Schema(description = "00. 임시 파일 업로드 요청 정보")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComFileTempDomainRequest {

    @Schema(description = "파일이 저장된 경로", example = "/uploads/2025/01/")
    private String filePath;

    @Builder.Default
    @Schema(description = "파일 크기 (Byte 단위)", example = "1048576")
    private long fileSize = 10L;


    @Schema(description = "파일 제한을 사용할 타입(IMAGE,DOCUMENT,AUDIO,VIDEO) - ComFileType 참조", example = "IMAGE")
    private String fileType;

    @Schema(description = "파일 고유 ID", example = "FILE_123456")
    private String fileId;

    public ComFileTempDomain toDomain() {
        return ComFileTempDomain.builder()
                .filePath(this.filePath)
                .fileSize(this.fileSize)
//                .fileType(this.fileType)
                .fileId(this.fileId)
                .build();
    }

}
