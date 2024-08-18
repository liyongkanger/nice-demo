package com.lyk.dto.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EsConstants {

    public static final String CONFIG_PREFIX = "kyp.es";

    public static final String ENABLED = CONFIG_PREFIX + ".enable";

    public static final String TYPE = "type";
    public static final String FIELDS = "fields";
    public static final String KEYWORD = "keyword";
    public static final String PROPERTIES = "properties";
    public static final String ANALYZER ="analyzer";
    public static final String INDEX_SHARD_NUM = "index.number_of_shards";
    public static final String INDEX_REPLICAS_NUM = "index.number_of_replicas";
}
