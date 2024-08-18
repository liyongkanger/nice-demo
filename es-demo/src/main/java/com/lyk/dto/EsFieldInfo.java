package com.lyk.dto;

import lombok.Data;

/**
 * ES的字段信息
 */
@Data
public class EsFieldInfo {
    //字段类型
    private FieldTypeEnum type;
    //字段名称
    private String name;
    //分析器
    private String analyzer;
}
