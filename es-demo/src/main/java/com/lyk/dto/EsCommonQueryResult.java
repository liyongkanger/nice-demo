package com.lyk.dto;

import lombok.Data;

import java.util.List;

@Data
public class EsCommonQueryResult<T> extends PageRequest{
    // json数据
    List<T> datas;
}
