package com.lyk.reddison;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

public class Test {

    private static final String LOCK_KEY = "LOCK_KEY";

    @Autowired
    private Redisson reddison;
    /**
     * 分布式锁 使用场景：
     * 前提： 某集群服务提供一组任务：
     * A请求随机从集群中的机器1任务组中获取一个任务;
     * B请求随机从集群中的机器2任务组中获取一个任务；
     * 不做处理可能A请求和B请求挑中了同一个任务的情况
     *
     * 2. 不同流量的？
     * 3. 并发扣减 集群的时候
     *
     */
    public String detuckStock() {
        RLock lock = reddison.getLock(LOCK_KEY);


        return null;
    }


}
