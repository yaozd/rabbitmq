package com.space.rbq.store.taskExt;

import com.space.rbq.store.threadExt.AbstractTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PayTask extends AbstractTask {

    private String value;

    public PayTask(ArrayBlockingQueue<Integer> tokenBucket, String value) {
        super(tokenBucket, value);
        this.value = value;
    }

    @Override
    protected void doWork() {
        log.info(Thread.currentThread().getName());
        try {
            //具体的业务处理逻辑
            //任务操作异常或数据库异常
            //TimeUnit.SECONDS.sleep(30);
            TimeUnit.SECONDS.sleep(10);
            log.debug("222");
            if (log.isDebugEnabled()) {
                log.debug("从reids消息队列的值获得的值value=" + value);
            }
        } catch (Exception ex) {
            //log ex
        }
    }
}
