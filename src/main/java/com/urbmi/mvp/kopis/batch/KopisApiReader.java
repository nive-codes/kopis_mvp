package com.urbmi.mvp.kopis.batch;
import com.urbmi.mvp.kopis.dto.KopisListRequest;
import com.urbmi.mvp.kopis.domain.KopisListDomain;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author nive
 * @class KopisApiReader
 * @desc KopisApi와 통신해 데이터를 받아온다.
 * @since 2025-02-21
 */
@Component
public class KopisApiReader implements ItemReader<List<KopisListDomain>> {
  private final RestTemplate restTemplate;
  private final String apiUrl = "http://www.kopis.or.kr/openApi/restful/pblprfr";

  public KopisApiReader(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  @Override
  public List<KopisListDomain> read() throws Exception {
    // 요청 DTO 설정
    KopisListRequest request = KopisListRequest.builder()
            .stdate("20250101")
            .eddate("20250131")
            .cpage("1")
            .rows("100")
            .build();

    // API 요청 URL 구성
    String url = String.format("%s?service={serviceKey}&stdate={stdate}&eddate={eddate}&rows={rows}&cpage={cpage}",
            apiUrl, "YOUR_SERVICE_KEY", request.getStdate(), request.getEddate(), request.getRows(), request.getCpage());

    // API 호출
    KopisListDomain[] response = restTemplate.getForObject(url, KopisListDomain[].class);

    // 결과 반환
    return response != null ? List.of(response) : null;
  }

}
