package com.urbmi.mvp.kopis.batch;
import com.urbmi.mvp.kopis.domain.KopisListDomain;
import com.urbmi.mvp.kopis.mapper.KopisMapper;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @author nive
 * @class KopisApiWriter
 * @desc [클래스 설명]
 * @since 2025-02-21
 */
public class KopisApiWriter implements ItemWriter<List<KopisListDomain>> {

    private final KopisMapper kopisMapper;

    public KopisApiWriter(KopisMapper kopisMapper) {
        this.kopisMapper = kopisMapper;
    }

    @Override
    public void write(Chunk<? extends List<KopisListDomain>> chunk) throws Exception {
        for (List<KopisListDomain> item : chunk) {
            for (KopisListDomain domain : item) {
                // DB에 저장하는 로직
                kopisMapper.kopisListSave(domain);
            }
        }
    }
}