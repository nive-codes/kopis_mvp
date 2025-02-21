package com.urbmi.mvp.kopis.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author nive
 * @class KopisDomain
 * @desc 공연 정보 도메인
 * @since 2025-02-21
 */
@Builder
@Data
public class KopisDetlDomain {

  private String mt20id;          // 공연 ID PF132236
  private String mt10id;          // 공연시설 ID FC001431
  private String prfnm;           // 공연명 우리연애할까
  private String prfpdfrom;       // 공연시작일 2016.05.12
  private String prfpdto;         // 공연종료일 2016.07.31
  private String fcltynm;         // 공연시설명(공연장명) 피가로아트홀(구 훈아트홀) (피가로아트홀)
  private String prfcast;         // 공연출연진 김부연, 임정균, 최수영
  private String prfcrew;         // 공연제작진 천정민
  private String prfruntime;      // 공연 런타임 1 시간 30 분
  private String prfage;          // 공연 관람 연령 만 12 세 이상
  private String entrpsnmP;       // 제작사 극단 피에로
  private String entrpsnmA;       // 기획사
  private String entrpsnmH;       // 주최
  private String entrpsnmS;       // 주관
  private String pcseguidance;    // 티켓가격 전석 30,000 원
  private String poster;          // 포스터 경로 http://www.kopis.or.kr/upload/pfmPoster/PF_PF132236_160704_142630.gif
  private String sty;             // 줄거리
  private String genrenm;         // 공연장르명 연극
  private String prfstate;        // 공연상태 공연중
  private String openrun;         // 오픈런 N
  private String visit;           // 내한 N
  private String child;           // 아동 N
  private String daehakro;        // 대학로 Y
  private String festival;        // 축제 N
  private String musicallicense;  // 뮤지컬 라이센스 N
  private String musicalcreate;   // 뮤지컬 창작 N
  private String updatedate;      // 최종수정일 2019-07-25 10:03:14

  private List<String> styurls;  // 소개 이미지 목록(styurl의 객체) 여러 개의 styurl을 담을 리스트
  private String dtguidance;     // 공연시간 화요일 ~ 금요일(20:00), 토요일(16:00,19:00), 일요일(15:00,18:00)
}
