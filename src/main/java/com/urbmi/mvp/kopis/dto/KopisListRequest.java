package com.urbmi.mvp.kopis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author nive
 * @class kopisListRequest
 * @desc 공연목록 조회 오퍼레이션 - pblpfrRequest
 * @since 2025-02-21
 */
@Builder
@Data
@AllArgsConstructor
@NotBlank
public class KopisListRequest {

    @NotNull
    @Builder.Default
    private String stdate="20250101";  //공연시작일 20230108

    @NotNull
    @Builder.Default
    private String eddate ="20250131";  //공연종료일 20230112(최대 31일)

    @NotNull
    @Builder.Default
    private String cpage = "1";   //현재페이지 1

    @NotNull
    @Builder.Default
    private String rows = "100";   //페이지당 목록 수 1 최대 100


    private String shcate;  // 장르코드  AAAA

    private String shprfnm; // 공연명  사랑(URLEncoding)

    private String shprfnmfct;  //  공연시설명 예술의전당(URLEncoding)

    private String prfplccd;    // 공연장코드 FC000003-01

    private String signgucode;  // 지역(시도)코드  11

    private String signgucodesub;   // 지역(구군)코드  1111

    private String kidstate;    // 아동공연여부  Y(지정안하면 디폴트는 ‘N’)

    private String prfstate;    // 공연상태코드  01

    private String openrun; // 오픈런여부  Y

    private String afterdate;   // 해당일자 이후     등록/수정된 항목만 출력 20230101



}
