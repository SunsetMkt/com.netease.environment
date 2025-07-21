package com.netease.ntunisdk.modules.clientlog.constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes6.dex */
public class ClientLogThreadPool {
    private static volatile ClientLogThreadPool instance;
    private ExecutorService executorService;

    public static ClientLogThreadPool getInstance() {
        if (instance == null) {
            synchronized (ClientLogThreadPool.class) {
                if (instance == null) {
                    instance = new ClientLogThreadPool();
                }
            }
        }
        return instance;
    }

    private ClientLogThreadPool() {
    }

    public void init() {
        this.executorService = new ThreadPoolExecutor(2, 3, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }
}