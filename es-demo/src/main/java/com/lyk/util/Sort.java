package com.lyk.util;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sort basePageDto 排序
 *
 * @author huangfl34500
 * @date 2022/7/11 10:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sort {

    List<Order> orders;

    @Override
    public String toString() {
        return orders.stream().map(o -> o.getProperty() + StrUtil.SPACE + o.getDirection()).collect(Collectors.joining(StrUtil.COMMA));
    }

    public static Sort by(String... properties) {
        Assert.notNull(properties, "Properties must not be null!");
        if (properties.length == 0) {
            return new Sort();
        }
        return new Sort(
                Arrays.stream(properties)
                        .map(o -> StrUtil.splitTrim(o, StrUtil.COMMA))
                        .flatMap(List::stream)
                        .map(Order::of)
                        .collect(Collectors.toList()));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {

        public static final org.springframework.data.domain.Sort.Direction DEFAULT_DIRECTION = org.springframework.data.domain.Sort.Direction.ASC;

        private String property;
        private org.springframework.data.domain.Sort.Direction direction;

        public static Order of(String property) {
            List<String> res = cn.hutool.core.util.StrUtil.split(property, StrUtil.SPACE);
            return res.size() == 1 ? new Order(res.get(0), Order.DEFAULT_DIRECTION)
                    : new Order(res.get(0), org.springframework.data.domain.Sort.Direction.fromString(res.get(1)));
        }
    }
}
