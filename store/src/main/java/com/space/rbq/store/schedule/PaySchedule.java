package com.space.rbq.store.schedule;

import com.space.rbq.store.taskExt.PayTask;
import com.space.rbq.store.threadExt.WorkThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

@Slf4j
//@Component
public class PaySchedule {
    private String tokenBucketName="pay";
    private int maxThreadSize=2;
    @Scheduled(initialDelay = 1000, fixedDelay = 10 * 5)
    public void doWork() throws InterruptedException {
        log.info("1");
        WorkThreadPool task_readQueue_threadPool=new WorkThreadPool(tokenBucketName,maxThreadSize);
        SynchronousQueue<String> data = new SynchronousQueue<String>(true);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(()->{
            for (int i = 0; i <1000 ; i++) {
                try {
                    //相当于令牌桶-通过令牌来控制有效读取的任务数等于可运行的处理的线程数
                    task_readQueue_threadPool.getTokenBucket().put(1);
                    data.put(String.valueOf(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        while (true){
            String value=data.take();
            log.info("当前值value="+value);
            PayTask task1=new PayTask(task_readQueue_threadPool.getTokenBucket(),value);
            task_readQueue_threadPool.getExecutor().execute(task1);
        }
    }
}
