package com.jts.smallbox.buz.dao;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.jts.smallbox.mybatis.interceptor.MybatisInnerInterceptor;
import com.jts.smallbox.mybatis.interceptor.MybatisInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * @author jts
 */
@Configuration
@MapperScans(value = {@MapperScan("com.jts.smallbox.buz.dao")})
public class MyBatisPlusConfig {

    @Value("classpath:sql/init-dev.sql")
    private Resource dbScriptDev;

    @Value("classpath:sql/init-shardingsphere.sql")
    private Resource dbScriptShardingsphere;

    @Bean("databasePopulator")
    @ConditionalOnProperty(value = "spring.profiles.active", havingValue = "dev", matchIfMissing = true)
    public DatabasePopulator populatorByDev() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(dbScriptDev);
        return populator;
    }

    @Bean("databasePopulator")
    @ConditionalOnProperty(value = "spring.profiles.active", havingValue = "shardingsphere")
    public DatabasePopulator populatorByShardingsphere() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(dbScriptShardingsphere);
        return populator;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource, final DatabasePopulator databasePopulator) {
        DataSourceInitializer init = new DataSourceInitializer();
        init.setDataSource(dataSource);
        init.setDatabasePopulator(databasePopulator);
        return init;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.addInterceptor(new MybatisInterceptor());
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        interceptor.addInnerInterceptor(new MybatisInnerInterceptor());
        return interceptor;
    }

}
