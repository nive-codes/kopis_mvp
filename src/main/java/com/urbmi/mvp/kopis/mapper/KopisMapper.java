package com.urbmi.mvp.kopis.mapper;

import com.urbmi.mvp.kopis.domain.KopisDetlDomain;
import com.urbmi.mvp.kopis.domain.KopisListDomain;
import com.urbmi.mvp.kopis.dto.KopisListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jdbc.repository.query.Query;

@Mapper
public interface KopisMapper {
    void kopisListSave(KopisListDomain domain);
    void kopisDetlUpdate(KopisDetlDomain domain);

    @Query("select count(*) from kopis_performance")
    int kopisCountSave(KopisListRequest request);
}
