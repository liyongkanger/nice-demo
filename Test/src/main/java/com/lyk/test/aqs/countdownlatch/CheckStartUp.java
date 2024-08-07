package com.lyk.test.aqs.countdownlatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckStartUp {

    private static List<DangerCenter> stateList;
    private static CountDownLatch countDown;

    public static void main(String[] args) throws InterruptedException {
        checkAllStations();
        System.exit(  0);
    }

    private static void checkAllStations() throws InterruptedException {

        countDown = new CountDownLatch(2);
        stateList = new ArrayList<>();
        stateList.add(new StationBeijingIMooc(countDown));
        stateList.add(new StationShanghai(countDown));

        // 使用线程池
        ExecutorService executorService = Executors.newFixedThreadPool(stateList.size());
        for (DangerCenter dangerCenter : stateList) {
            executorService.execute(dangerCenter);
        }
        countDown.await();

        for (DangerCenter dangerCenter : stateList) {
            if (!dangerCenter.isOk()) {
                System.out.println("紧急通知：" + dangerCenter.getStation() + "站点异常");
            }
        }
        System.out.println("检查完毕");

    }

}
