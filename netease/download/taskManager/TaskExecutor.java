package com.netease.download.taskManager;

import com.netease.download.util.LogUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public class TaskExecutor {
    public static final String TAG = "TaskExecutor";
    private static TaskExecutor sTaskExecutor;
    private ExecutorService mFixedThreadPool = null;
    private ExecutorService mSingleThreadExecutor = null;

    private TaskExecutor() {
    }

    public static TaskExecutor getInstance() {
        if (sTaskExecutor == null) {
            LogUtil.i("TaskExecutor", "TaskExecutor [getFixedThreadPool] new TaskExecutor()");
            sTaskExecutor = new TaskExecutor();
        }
        return sTaskExecutor;
    }

    public ExecutorService getFixedThreadPool() {
        if (this.mFixedThreadPool == null) {
            LogUtil.i("TaskExecutor", "TaskExecutor [getFixedThreadPool] newFixedThreadPool");
            this.mFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        return this.mFixedThreadPool;
    }

    public ExecutorService getSingleThreadExecutor() {
        if (this.mSingleThreadExecutor == null) {
            LogUtil.i("TaskExecutor", "TaskExecutor [getFixedThreadPool] newSingleThreadExecutor");
            this.mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        return this.mSingleThreadExecutor;
    }

    public void closeFixedThreadPool() {
        ExecutorService executorService = this.mFixedThreadPool;
        if (executorService == null || executorService.isShutdown()) {
            return;
        }
        this.mFixedThreadPool.shutdownNow();
        this.mFixedThreadPool = null;
    }
}