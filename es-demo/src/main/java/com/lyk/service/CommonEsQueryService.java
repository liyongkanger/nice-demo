package com.lyk.service;

import com.lyk.dto.EsCommonQueryInfo;
import com.lyk.dto.EsCommonQueryResult;

/**
 * 通用查询
 */
public interface CommonEsQueryService {

    EsCommonQueryResult<String> commonQuery(EsCommonQueryInfo queryInfo);

    <T> EsCommonQueryResult<T> commonQuery(EsCommonQueryInfo queryInfo, Class<T> clazz);

}
