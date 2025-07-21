package com.netease.ngrtc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes4.dex */
public class TaskSubmitter {
    final ExecutorService m_executorService = Executors.newSingleThreadExecutor();

    public Future submit(Runnable runnable) {
        if (this.m_executorService.isTerminated() || this.m_executorService.isShutdown() || runnable == null) {
            return null;
        }
        return this.m_executorService.submit(runnable);
    }

    public void shutdown() {
        this.m_executorService.shutdown();
    }
}