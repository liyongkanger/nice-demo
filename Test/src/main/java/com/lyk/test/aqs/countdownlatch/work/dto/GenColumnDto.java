package com.lyk.test.aqs.countdownlatch.work.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenColumnDto {
    /**
     * 字段长度
     */
    private String columnLength;
    private String columnType;
    private String columnComment;
    private String columnKey;
    private String dealMode;

}
