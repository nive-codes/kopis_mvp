package com.urbmi.mvp.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.urbmi.mvp"},annotationClass = org.apache.ibatis.annotations.Mapper.class) //해당 패키지에서 mapper 어노테이션만 scan
@Slf4j
public class MybatisConfig {

    /*yml명시한 값 - mapper-locations*/
    @Value("${spring.mybatis.mapper-locations}")
    private String mapperLocations;

    /*yml명시한 값 - type-aliases-package*/
    @Value("${spring.mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    // MyBatis 프레임워크 동작 설정 코드
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // 1. SqlSessionFactoryBean 객체 생성
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 2. DataSource 설정, 매퍼 파일 위치 설정
        sessionFactory.setDataSource(dataSource);

        // 매퍼 XML 파일 경로 설정
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));

        // TypeAliases 설정
        sessionFactory.setTypeAliasesPackage(typeAliasesPackage);

        // MyBatis 설정
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 스네이크 케이스를 카멜 케이스로 자동 매핑
        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }

    /*@Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }*/
}