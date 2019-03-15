package com.space.rbq.store.threadExt;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by zd.yao on 2017/7/7.
 */
public abstract class AbstractTask implements Runnable {
    ArrayBlockingQueue<Integer> tokenBucket;
    String value;

    public AbstractTask(ArrayBlockingQueue<Integer> tokenBucket, String value) {
        this.tokenBucket = tokenBucket;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            doWork();
        } finally {
            tokenBucket.poll();
        }
    }

    protected void doWork() {
        //具体的业务逻辑代码
        throw new IllegalStateException("【AbstractTask】没有具体的业务逻辑实现代码");
    }
}