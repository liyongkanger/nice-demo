package com.lyk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsIndexMappingInfo {
    private List<EsFieldInfo> fieldInfos = new ArrayList<>();
}
