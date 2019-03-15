package com.space.rbq.store.test4mq.taskExt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
@Slf4j
public enum TaskConfigEnum {
    PAY(2);

    TaskConfigEnum(Integer maxThreadSize) {
        this.tokenBucket = new ArrayBlockingQueue<Integer>(maxThreadSize);
        this.data = new SynchronousQueue<String>(true);
    }

    private ArrayBlockingQueue<Integer> tokenBucket;
    private SynchronousQueue<String> data;

    public void putData(String data) {
        try {
            this.data.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String takeData() {
        try {
            return this.data.take();
        } catch (InterruptedException e) {
            boolean isInterrupted= Thread.currentThread().isInterrupted();
            log.info("isInterrupted="+isInterrupted);
            throw new IllegalStateException(e);
        }
    }

    public void putToken() {
        try {
            this.tokenBucket.put(1);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void pollToken() {
        this.tokenBucket.poll();
    }

    public void sendData(String data) {
        putData(data);
        putToken();
    }
    public ArrayBlockingQueue<Integer> getTokenBucket(){
        return this.tokenBucket;
    }
}