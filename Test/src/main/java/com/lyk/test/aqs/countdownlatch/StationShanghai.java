package com.lyk.test.aqs.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class StationShanghai extends DangerCenter{


    public StationShanghai(CountDownLatch countDown) {
        super(countDown,"上海", false );
    }

    @Override
    public void check() throws InterruptedException {
        System.out.println("上海站开始检查" + this.getStation());
        // 业务操作
        Thread.sleep(2000);
        System.out.println("上海站检查完毕" + this.getStation());
    }
}
