package com.lyk.dto.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * es配置属性
 * @author yinxiang11600 huangfl34500
 * @date 2022/11/17 14:08
 */
@Getter
@Setter
public final class EsProperties {
    private boolean enable = false;
    @NestedConfigurationProperty
    private ContainerProperties container = new ContainerProperties();
    @NestedConfigurationProperty
    private IndexProperties index = new IndexProperties();

    @Getter
    @Setter
    public static class IndexProperties {
        private String name;
        private int shard = 1;
        private int replica = 1;
        private int from = 0;
        private int size = 2000;
        private int timeout = 60;
    }

    @Getter
    @Setter
    public static class ContainerProperties {
        private String imageUrl;
        private String version;
    }

}
