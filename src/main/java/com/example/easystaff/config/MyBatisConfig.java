package com.example.easystaff.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 显式配置 MyBatis 所需的 SqlSessionFactory 和 SqlSessionTemplate，
 * 解决 Mapper 需要的 sqlSessionFactory/sqlSessionTemplate 注入问题。
 */
@Configuration
public class MyBatisConfig {

    /**
     * SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 对应 application.properties 中的 mybatis.mapper-locations
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));

        // 对应 mybatis.type-aliases-package
        factoryBean.setTypeAliasesPackage("com.example.easystaff.domain");

        return factoryBean.getObject();
    }

    /**
     * SqlSessionTemplate
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


