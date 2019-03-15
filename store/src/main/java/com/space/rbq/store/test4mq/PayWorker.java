package com.space.rbq.store.test4mq;

import com.space.rbq.store.test4mq.taskExt.AbstractWorker;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PayWorker extends AbstractWorker {

    private String value;

    public PayWorker(ArrayBlockingQueue<Integer> tokenBucket, String value) {
        super(tokenBucket);
        this.value = value;
    }

    @Override
    protected void doWork() {
        log.info(Thread.currentThread().getName());
        try {
            //具体的业务处理逻辑
            //任务操作异常或数据库异常
            //TimeUnit.SECONDS.sleep(30);
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception ex) {
            //log ex
        }
    }
}
