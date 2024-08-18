package com.lyk.config;

import cn.easyes.starter.config.EsAutoConfiguration;
import com.lyk.dto.constant.EsProperties;
import com.lyk.service.CommonEsMngService;
import com.lyk.service.CommonEsQueryService;
import com.lyk.service.impl.CommonEsMngServiceImpl;
import com.lyk.service.impl.CommonEsQueryServiceImpl;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ESAutoConfig
 *
 * @author yinxiang11600 huangfl34500
 * @date 2022/10/11 14:07
 */
@Configuration
@AutoConfigureBefore({ElasticsearchRestClientAutoConfiguration.class})
@EnableConfigurationProperties(EsProperties.class)
public class ESAutoConfig {
    @Autowired
    EsProperties properties;



    @Configuration
    @Import(EsAutoConfiguration.class)
    static class EasyEsConfig {

    }

    @Bean
    public CommonEsQueryService commonEsQueryService(RestHighLevelClient highLevelClient) {
        return new CommonEsQueryServiceImpl(highLevelClient);
    }

    @Bean
    public CommonEsMngService commonEsMngService(RestHighLevelClient highLevelClient) {
        return new CommonEsMngServiceImpl(properties, highLevelClient);
    }

}
