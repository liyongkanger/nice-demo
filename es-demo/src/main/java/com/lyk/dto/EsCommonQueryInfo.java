package com.lyk.dto;

import com.lyk.util.Sort;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EsCommonQueryInfo extends PageRequest {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 查询条件集合
     */
    private List<QueryFieldCondition> queryField;

    private List<String> columns;

    private Sort sort;

}
