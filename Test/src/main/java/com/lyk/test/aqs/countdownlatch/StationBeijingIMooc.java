package com.lyk.test.aqs.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class StationBeijingIMooc extends DangerCenter{


    public StationBeijingIMooc(CountDownLatch countDown) {
        super(countDown,"北京",false);
    }

    @Override
    public void check() throws InterruptedException {
        System.out.println("北京站开始检查" + this.getStation());
        // 业务操作
        Thread.sleep(2000);
        System.out.println("北京站检查完毕" + this.getStation());
    }
}
