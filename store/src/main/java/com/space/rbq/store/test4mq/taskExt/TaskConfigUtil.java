package com.space.rbq.store.test4mq.taskExt;

public class TaskConfigUtil {
    private static boolean shutdownState = false;

    /**
     * 关闭任务
     */
    public static void shutdown() {
        shutdownState = true;
    }

    /**
     * 是否为关闭状态
     *
     * @return
     */
    public static boolean isShutdown() {
        return shutdownState;
    }

    /**
     * 任务是否已经全部完成
     *
     * @return
     */
    public static boolean isCompleteTaskAll() {
        for (TaskConfigEnum e : TaskConfigEnum.values()) {
            if (e.getTokenBucket().size() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 当前任务数量
     *
     * @return
     */
    public static Integer getTaskSize() {
        Integer size = 0;
        for (TaskConfigEnum e : TaskConfigEnum.values()) {
            size = size + e.getTokenBucket().size();
        }
        return size;
    }
}
