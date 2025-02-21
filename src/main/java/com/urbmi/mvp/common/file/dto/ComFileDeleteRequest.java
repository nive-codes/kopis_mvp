package com.urbmi.mvp.common.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nive
 * @class ComFileDomain
 * @desc 파일 업로드 domain 객체
 * @since 2025-01-16
 */
@Schema(description = "01. 본 파일 삭제 요청 정보")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComFileDeleteRequest {

    @Schema(description = "파일 고유 ID", example = "FILE_123456")
    private String fileId;

    @Builder.Default
    @Schema(description = "파일 시퀀스 (동일한 파일 ID 내에서 순차적으로 증가)", example = "1")
    private Integer fileSeq = 0;
}
