package com.lyk.service;

import com.lyk.dto.EsIndexMappingInfo;

import java.io.IOException;

public interface CommonEsMngService {


    // 创建索引
    boolean createIndex(String indexName);

    boolean createIndexWithAlias(String indexName, String aliasName);
    boolean createIndexWithMapping(String indexName, EsIndexMappingInfo mappingInfo);
    boolean createIndexWithAliasAndMapping(String indexName,String alias, EsIndexMappingInfo mappingInfo) throws IOException;


}
