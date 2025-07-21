package com.netease.pharos.threadManager;

import com.netease.pharos.util.LogUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes6.dex */
public class ThreadPoolManager {
    public static final String TAG = "ThreadPoolManager";
    private static ThreadPoolManager sThreadPoolManager;
    private ExecutorService mFixedThreadPool = null;
    private ExecutorService mSingleThreadExecutor = null;

    private ThreadPoolManager() {
    }

    public static ThreadPoolManager getInstance() {
        if (sThreadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (sThreadPoolManager == null) {
                    LogUtil.i(TAG, "ThreadPoolManager [getInstance] new ThreadPoolManager");
                    sThreadPoolManager = new ThreadPoolManager();
                }
            }
        }
        return sThreadPoolManager;
    }

    public ExecutorService getFixedThreadPool() {
        ExecutorService executorService;
        synchronized (ThreadPoolManager.class) {
            ExecutorService executorService2 = this.mFixedThreadPool;
            if (executorService2 == null || executorService2.isShutdown() || this.mFixedThreadPool.isTerminated()) {
                LogUtil.i(TAG, "ThreadPoolManager [getFixedThreadPool] newFixedThreadPool");
                this.mFixedThreadPool = Executors.newFixedThreadPool(3);
            }
            executorService = this.mFixedThreadPool;
        }
        return executorService;
    }

    public ExecutorService getSingleThreadExecutor() {
        synchronized (ThreadPoolManager.class) {
            ExecutorService executorService = this.mSingleThreadExecutor;
            if (executorService == null || executorService.isShutdown() || this.mSingleThreadExecutor.isTerminated()) {
                LogUtil.i(TAG, "ThreadPoolManager [getFixedThreadPool] newSingleThreadExecutor");
                this.mSingleThreadExecutor = Executors.newSingleThreadExecutor();
            }
        }
        return this.mSingleThreadExecutor;
    }

    public void closeFixedThreadPool() {
        synchronized (ThreadPoolManager.class) {
            ExecutorService executorService = this.mFixedThreadPool;
            if (executorService != null && !executorService.isShutdown()) {
                this.mFixedThreadPool.shutdownNow();
            }
            this.mFixedThreadPool = null;
        }
    }
}