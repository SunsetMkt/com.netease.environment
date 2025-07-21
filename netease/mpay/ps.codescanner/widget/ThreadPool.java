package com.netease.mpay.ps.codescanner.widget;

import android.os.Build;
import com.netease.mpay.ps.codescanner.utils.Logging;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes5.dex */
public class ThreadPool {
    private static AtomicInteger poolNumber = new AtomicInteger(1);
    private static ExecutorService sAsyncTaskThreadPool;
    private static ExecutorService sCustomThreadPool;
    private int corePoolSize;
    private int maximumPoolSize;
    private int priority = 10;
    private long keepAliveMilliseconds = 0;
    private int blockQueueSize = 64;

    public static synchronized ExecutorService getAsyncTaskThreadPoolInstance() {
        if (sAsyncTaskThreadPool == null) {
            sAsyncTaskThreadPool = new ThreadPool(1, 1).build();
        }
        return sAsyncTaskThreadPool;
    }

    public static synchronized ExecutorService getCustomThreadPoolInstance() {
        if (sCustomThreadPool == null) {
            int iAvailableProcessors = Runtime.getRuntime().availableProcessors() / 2;
            if (iAvailableProcessors < 1) {
                iAvailableProcessors = 1;
            } else if (iAvailableProcessors > 3) {
                iAvailableProcessors = 3;
            }
            sCustomThreadPool = new ThreadPool(iAvailableProcessors, iAvailableProcessors * 2).setPriority(5).setKeepAlive(600000L).setBlockQueue(50).build();
        }
        return sCustomThreadPool;
    }

    private ThreadPool(int i, int i2) {
        this.corePoolSize = i;
        this.maximumPoolSize = i2;
    }

    private ThreadPool setPriority(int i) {
        if (10 == i || 1 == i || 5 == i) {
            this.priority = i;
        }
        return this;
    }

    private ThreadPool setKeepAlive(long j) {
        if (j >= 0) {
            this.keepAliveMilliseconds = j;
        }
        return this;
    }

    private ThreadPool setBlockQueue(int i) {
        if (i >= 0) {
            this.blockQueueSize = i;
        }
        return this;
    }

    private ExecutorService build() {
        int i = this.corePoolSize;
        int i2 = this.maximumPoolSize;
        long j = this.keepAliveMilliseconds;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        int i3 = this.blockQueueSize;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(i, i2, j, timeUnit, i3 > 0 ? new LinkedBlockingQueue(i3) : new LinkedBlockingQueue(), new CThreadFactory(), new CRejectedExecution());
        if (this.keepAliveMilliseconds > 0 && Build.VERSION.SDK_INT >= 9) {
            try {
                threadPoolExecutor.allowCoreThreadTimeOut(true);
            } catch (IllegalArgumentException e) {
                Logging.logStackTrace(e);
            } catch (Exception e2) {
                Logging.logStackTrace(e2);
            }
        }
        return threadPoolExecutor;
    }

    private class CThreadFactory implements ThreadFactory {
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix = "MPT-p" + ThreadPool.poolNumber.getAndIncrement() + "-t";

        CThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(null, runnable, this.namePrefix + this.threadNumber.getAndIncrement());
            thread.setPriority(ThreadPool.this.priority);
            return thread;
        }
    }

    private class CRejectedExecution implements RejectedExecutionHandler {
        private CRejectedExecution() {
        }

        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            Logging.log("task has been discarded");
        }
    }
}