package com.lyk.product;

import com.lyk.kafka.Application;
import com.lyk.kafka.producer.Demo01Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Demo01 {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Demo01Producer producer;

    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        int id = (int) (System.currentTimeMillis()/ 1000);
        SendResult result = producer.syncSend(id);
        log.info("[testSyncSend][发送编号：[{}] 发送结果：[{}]]", id, result);

        // 阻塞等待保证消费
        new CountDownLatch(1).await();
    }

    public void testAsynSend() throws InterruptedException {
        int id = (int) (System.currentTimeMillis() / 1000);
        producer.asyncSend(id).addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("[testAsynSend][发送编号：[{}] 发送异常]]", id, throwable);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                log.info("[testAsynSend][发送编号：[{}] 发送异常]]", id, result);
            }
        });
        new CountDownLatch(1).await();
    }

    public static void main(String[] args) {
        String str = "A | 20340720 | 040004 | Hua An Bao Li Balanced Fund                                                                                                                                                                              | 华安宝利 | 华安宝利                                           | 50040000 | 006 | 交通银行 | 004 | 中国银行 | CP000000 | 20040716 | 20040817 |   | 04 |  |   | CNY | ON | 20200829 | 1 |  | 01 | 公募基金 | F04 | 混合型 | PUB |   | error | 0.00 | 20181009 | N | N |   | N | N | N | Y | N | N | N |  | 20180830 | 204852 |   |   | \n";
        String[] split = str.split("\\s*\\|\\s*");
        for(int i = 0; i < split.length; i++) {
            System.out.println( i + "  ======>  " + split[i]);
        }
    }



}
