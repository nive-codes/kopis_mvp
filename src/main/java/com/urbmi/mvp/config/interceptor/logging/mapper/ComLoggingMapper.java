package com.urbmi.mvp.config.interceptor.logging.mapper;

import com.urbmi.mvp.config.interceptor.logging.domain.ComLoggingDomain;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComLoggingMapper {

    void insertLog(ComLoggingDomain comLoggingDomain);
}
