package com.lyk.kafka.message;

import cn.hutool.core.date.DateUtil;

public class Demo01Message {

    public static final String TOPIC = "DEMO_01";

    private Integer id;

    @Override
    public String toString() {
        return "Demo01Message{" +
                "id=" + id +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
