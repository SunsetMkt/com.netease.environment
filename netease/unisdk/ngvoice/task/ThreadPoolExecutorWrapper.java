package com.netease.unisdk.ngvoice.task;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes6.dex */
public class ThreadPoolExecutorWrapper {
    private Handler mMainHandler;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private ExecutorService mThreadPoolExecutor;

    public ThreadPoolExecutorWrapper(int i, int i2, int i3) {
        this.mThreadPoolExecutor = new ThreadPoolExecutor(i, i2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Executors.defaultThreadFactory());
        if (i3 > 0) {
            this.mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(i3);
        }
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    public void executeTask(Runnable runnable) {
        this.mThreadPoolExecutor.execute(runnable);
    }

    public <T> Future<T> submitTask(Callable<T> callable) {
        return this.mThreadPoolExecutor.submit(callable);
    }

    public void scheduleTask(long j, Runnable runnable) {
        this.mScheduledThreadPoolExecutor.schedule(runnable, j, TimeUnit.MILLISECONDS);
    }

    public void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long j, long j2, Runnable runnable) {
        this.mScheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, j, j2, TimeUnit.MILLISECONDS);
    }

    public void scheduleTaskAtFixedRateIncludingTaskRunningTime(long j, long j2, Runnable runnable) {
        this.mScheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable, j, j2, TimeUnit.MILLISECONDS);
    }

    public boolean removeScheduledTask(Runnable runnable) {
        return this.mScheduledThreadPoolExecutor.remove(runnable);
    }

    public void scheduleTaskOnUiThread(long j, Runnable runnable) {
        this.mMainHandler.postDelayed(runnable, j);
    }

    public void removeScheduledTaskOnUiThread(Runnable runnable) {
        this.mMainHandler.removeCallbacks(runnable);
    }

    public void runTaskOnUiThread(Runnable runnable) {
        this.mMainHandler.post(runnable);
    }

    public void shutdown() {
        ExecutorService executorService = this.mThreadPoolExecutor;
        if (executorService != null) {
            executorService.shutdown();
            this.mThreadPoolExecutor = null;
        }
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = this.mScheduledThreadPoolExecutor;
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
            this.mScheduledThreadPoolExecutor = null;
        }
    }
}