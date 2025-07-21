package com.netease.unisdk.ngvoice.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/* loaded from: classes6.dex */
public class TaskExecutor {
    private static ThreadPoolExecutorWrapper sThreadPoolExecutorWrapper;

    public static void init(int i, int i2, int i3) {
        if (sThreadPoolExecutorWrapper == null) {
            sThreadPoolExecutorWrapper = new ThreadPoolExecutorWrapper(i, i2, i3);
        }
    }

    public static void executeTask(Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.executeTask(runnable);
        }
    }

    public static <T> Future<T> submitTask(Callable<T> callable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            return threadPoolExecutorWrapper.submitTask(callable);
        }
        return null;
    }

    public static void scheduleTask(long j, Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.scheduleTask(j, runnable);
        }
    }

    public static void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long j, long j2, Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.scheduleTaskAtFixedRateIgnoringTaskRunningTime(j, j2, runnable);
        }
    }

    public static void scheduleTaskAtFixedRateIncludingTaskRunningTime(long j, long j2, Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.scheduleTaskAtFixedRateIncludingTaskRunningTime(j, j2, runnable);
        }
    }

    public static boolean removeScheduledTask(Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            return threadPoolExecutorWrapper.removeScheduledTask(runnable);
        }
        return false;
    }

    public static void scheduleTaskOnUiThread(long j, Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.scheduleTaskOnUiThread(j, runnable);
        }
    }

    public static void removeScheduledTaskOnUiThread(Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.removeScheduledTaskOnUiThread(runnable);
        }
    }

    public static void runTaskOnUiThread(Runnable runnable) {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.runTaskOnUiThread(runnable);
        }
    }

    public static void shutdown() {
        ThreadPoolExecutorWrapper threadPoolExecutorWrapper = sThreadPoolExecutorWrapper;
        if (threadPoolExecutorWrapper != null) {
            threadPoolExecutorWrapper.shutdown();
            sThreadPoolExecutorWrapper = null;
        }
    }
}