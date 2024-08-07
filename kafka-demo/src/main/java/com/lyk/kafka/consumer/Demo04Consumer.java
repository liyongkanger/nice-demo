package com.lyk.kafka.consumer;

import com.lyk.kafka.message.Demo01Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Demo04Consumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = Demo01Message.TOPIC,
            groupId = "demo01-A-consumer-group" + Demo01Message.TOPIC)
    public void onMessage(ConsumerRecord<Integer,String> record) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), record);
        throw new RuntimeException("模拟消费失败");
    }

}
