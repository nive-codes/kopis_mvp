package com.urbmi.mvp.kopis.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author nive
 * @class KopisDetlRequest
 * @desc 공연목록 조회 오퍼레이션 - KopisDetlRequest
 * @since 2025-02-21
 */
@Builder
@Data
public class KopisDetlRequest {

    private String mt20id;    // 공연 ID PF132236

}
