package com.space.rbq.store.test4mq;

import com.space.rbq.store.test4mq.taskExt.TaskConfigEnum;
import com.space.rbq.store.test4mq.taskExt.TaskConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PayTaskSchedule {

    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void doWork() {
        if (TaskConfigUtil.isShutdown()) {
            return;
        }
        log.info("this is test");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        while (true) {
            if (TaskConfigUtil.isShutdown()) {
                return;
            }
            String data = TaskConfigEnum.PAY.takeData();
            log.info("Take=" + data);
            if (data == null) {
                continue;
            }
            PayWorker payWorker = new PayWorker(TaskConfigEnum.PAY.getTokenBucket(), data);
            executor.execute(payWorker);
        }
    }
}
