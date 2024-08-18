package com.lyk.dto;

import co.elastic.clients.elasticsearch.ml.ConditionOperator;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryFieldCondition {


    private String name;

    /**
     * 操作符
     */
    private FieldOperEnum oper;

    /**
     * 结果对象
     */
    private List<Object> values = new ArrayList<>();

    // 支持嵌套查询
    private List<QueryFieldCondition> conditions = new ArrayList<>();

    // 查询关系条件默认值为and
    private ConditionOperEnum condOper = ConditionOperEnum.AND;

}
