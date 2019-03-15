package com.space.rbq.store.test4mq.taskExt;

import java.util.concurrent.ArrayBlockingQueue;

public abstract class AbstractWorker implements Runnable {
    private ArrayBlockingQueue<Integer> tokenBucket;
    public AbstractWorker(ArrayBlockingQueue<Integer> tokenBucket) {
        this.tokenBucket = tokenBucket;
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
