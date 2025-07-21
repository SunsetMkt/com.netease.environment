package com.netease.androidcrashhandler.processCenter;

import com.netease.androidcrashhandler.util.LogUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes5.dex */
public class TaskExecutor {
    public static final String TAG = "TaskExecutor";
    private static TaskExecutor sTaskExecutor;
    private ExecutorService mFixedThreadPool = null;
    private ExecutorService mSingleThreadExecutor = null;

    private TaskExecutor() {
    }

    public static TaskExecutor getInstance() {
        if (sTaskExecutor == null) {
            LogUtils.i(LogUtils.TAG, "TaskExecutor [getFixedThreadPool] new TaskExecutor()");
            sTaskExecutor = new TaskExecutor();
        }
        return sTaskExecutor;
    }

    public ExecutorService getFixedThreadPool() {
        if (this.mFixedThreadPool == null) {
            LogUtils.i(LogUtils.TAG, "TaskExecutor [getFixedThreadPool] getFixedThreadPool");
            this.mFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        return this.mFixedThreadPool;
    }

    public synchronized ExecutorService getSingleThreadExecutor() {
        if (this.mSingleThreadExecutor == null) {
            LogUtils.i(LogUtils.TAG, "TaskExecutor [getFixedThreadPool] getSingleThreadExecutor");
            this.mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        return this.mSingleThreadExecutor;
    }

    public ExecutorService newSingleThreadExecutor() {
        LogUtils.i(LogUtils.TAG, "TaskExecutor [getFixedThreadPool] newSingleThreadExecutor");
        return Executors.newSingleThreadExecutor();
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