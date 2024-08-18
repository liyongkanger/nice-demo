package com.lyk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lyk.dto.EsFieldInfo;
import com.lyk.dto.EsIndexMappingInfo;
import com.lyk.dto.FieldTypeEnum;
import com.lyk.dto.constant.EsProperties;
import com.lyk.service.CommonEsMngService;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lyk.dto.constant.EsConstants.*;

@AllArgsConstructor
public class CommonEsMngServiceImpl implements CommonEsMngService {

    private final EsProperties properties;

    private final RestHighLevelClient highLevelClient;


    @Override
    public boolean createIndex(String indexName) {
        return false;
    }

    @Override
    public boolean createIndexWithAlias(String indexName, String aliasName) {
        return false;
    }

    @Override
    public boolean createIndexWithMapping(String indexName, EsIndexMappingInfo mappingInfo) {
        return false;
    }

    @Override
    public boolean createIndexWithAliasAndMapping(String indexName, String alias, EsIndexMappingInfo mappingInfo) throws IOException {
        boolean result = Boolean.FALSE;

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.settings(Settings.builder()
                .put(INDEX_SHARD_NUM, properties.getIndex().getShard())
                .put(INDEX_REPLICAS_NUM, properties.getIndex().getReplica()));
        if (StrUtil.isNotEmpty(alias)) {
            createIndexRequest.alias(new Alias(alias));
        }
        if (mappingInfo != null) {
            HashMap<String, Object> mapping = new HashMap<>();
            HashMap<String, Object> propmaps = new HashMap<>();
            List<EsFieldInfo> fieldInfos =
                    mappingInfo.getFieldInfos();
            fieldInfos.forEach(v -> {
                HashMap<Object, Object> fieldMaps = new HashMap<>();
                FieldTypeEnum type = v.getType();
                switch(type) {
                    case KEYWORD_TEXT:
                        fieldMaps.put(TYPE,FieldTypeEnum.TEXT.getType());
                        Map<String,Object> keyword = new HashMap<>();
                        Map<String,Object> kwFields = new HashMap<>();
                        kwFields.put(TYPE,FieldTypeEnum.KEYWORD.getType());
                        keyword.put(KEYWORD,kwFields);
                        fieldMaps.put(FIELDS,keyword);
                        break;
                    default:
                        fieldMaps.put(TYPE,v.getType().getType());
                }
                if(v.getAnalyzer() != null){
                    fieldMaps.put(ANALYZER,v.getAnalyzer());
                }
                propmaps.put(v.getName(),fieldMaps);
            });
            mapping.put(PROPERTIES, propmaps);
            createIndexRequest.mapping(mapping);
        }
        CreateIndexResponse createIndexResponse = highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        result = createIndexResponse.isAcknowledged();
        return result;
    }
}
