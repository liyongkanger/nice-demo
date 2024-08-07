package com.lyk.test.aqs.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 抽象类 检查流程每个基本相同
 */
public abstract class DangerCenter implements Runnable {

    /**
     * 计数器
     */
    private CountDownLatch countDown;
    private String station; // 调度
    private boolean ok; // 是否检查完毕

    public DangerCenter(CountDownLatch countDown, String station, boolean ok) {
        this.countDown = countDown;
        this.station = station;
        this.ok = false;
    }

    @Override
    public void run() {
        try{
            check();
            ok = true;
        } catch (Throwable t) {
            t.printStackTrace();
            ok = false;
        } finally {
            if (countDown != null) {
                countDown.countDown();
            }
        }

    }

    public CountDownLatch getCountDown() {
        return countDown;
    }

    public void setCountDown(CountDownLatch countDown) {
        this.countDown = countDown;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    // 抽象方法
    public abstract void check() throws InterruptedException;


}
