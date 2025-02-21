package com.urbmi.mvp.kopis.batch;

import com.urbmi.mvp.kopis.domain.KopisListDomain;
import com.urbmi.mvp.kopis.domain.KopisListDomain;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author nive
 * @class KopisApiProcessor
 * @desc [클래스 설명]
 * @since 2025-02-21
 */
@Component
public class KopisApiProcessor implements ItemProcessor<List<KopisListDomain>, List<KopisListDomain>> {
    @Override
    public List<KopisListDomain> process(List<KopisListDomain> items) throws Exception {
        // TODO 중복 공연 정보 조회 및 삭제 처리 필요(mapper에 추가)
        return items;
    }

}
