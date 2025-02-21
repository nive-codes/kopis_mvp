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
@Schema(description = "00. 임시 파일 삭제 시 필수 정보")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComFileTempDeleteRequest {

    @Builder.Default
    @Schema(description = "파일 시퀀스 (동일한 파일 ID 내에서 순차적으로 증가)", example = "1")
    private Integer fileSeq = 0;

    @Schema(description = "파일 고유 ID", example = "FILE_123456")
    private String fileId;

    public ComFileTempDomain toDomain() {
        return ComFileTempDomain.builder()
                .fileId(this.fileId)
                .fileSeq(this.fileSeq)
                .build();
    }

}
