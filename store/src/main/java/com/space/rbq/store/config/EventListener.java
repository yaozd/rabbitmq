package com.space.rbq.store.config;

import com.space.rbq.store.test4mq.taskExt.TaskConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 监听应用关闭的钩子
 * Created by zd.yao on 2017/9/15.
 */
public class EventListener implements ApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //应用关闭-kill PID 不要使用kill -9 PID
        if (applicationEvent instanceof ContextClosedEvent) {
            logger.info("当前任务数=" + TaskConfigUtil.getTaskSize());
            //关闭消息队列的读取任务
            TaskConfigUtil.shutdown();
            //
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        boolean isCompleted = TaskConfigUtil.isCompleteTaskAll();
                        if (isCompleted) return;
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            //region 通过shutdown+awaitTermination实现任务执行超时后终止
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //endregion
            logger.info("已优雅退出可以关闭应用程序");
            return;
        }
    }
}
