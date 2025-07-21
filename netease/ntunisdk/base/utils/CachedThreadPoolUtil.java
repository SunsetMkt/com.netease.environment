package com.netease.ntunisdk.base.utils;

import com.CCMsgSdk.ControlCmdType;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class CachedThreadPoolUtil {
    private static volatile CachedThreadPoolUtil cachedThreadPoolUtil;
    private String TAG = "CachedThreadPoolUtil";
    private ExecutorService m_executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue());

    public static CachedThreadPoolUtil getInstance() {
        if (cachedThreadPoolUtil == null) {
            synchronized (CachedThreadPoolUtil.class) {
                if (cachedThreadPoolUtil == null) {
                    cachedThreadPoolUtil = new CachedThreadPoolUtil();
                }
            }
        }
        return cachedThreadPoolUtil;
    }

    public synchronized void close() {
        UniSdkUtils.i(this.TAG, ControlCmdType.CLOSE);
        try {
            ExecutorService executorService = this.m_executorService;
            if (executorService != null) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            UniSdkUtils.e(this.TAG, "close exception\uff1a" + e.getMessage());
        }
    }

    public synchronized void exec(Runnable runnable) {
        ExecutorService executorService = this.m_executorService;
        if (executorService != null) {
            if (!executorService.isShutdown() && !this.m_executorService.isTerminated()) {
                try {
                    UniSdkUtils.i(this.TAG, "exec");
                    this.m_executorService.execute(runnable);
                    return;
                } catch (Exception e) {
                    UniSdkUtils.e(this.TAG, "ExecutorService.execute exception:" + e.getMessage());
                    return;
                }
            }
            UniSdkUtils.e(this.TAG, "ExecutorService have shutdown");
            return;
        }
        UniSdkUtils.e(this.TAG, "ExecutorService null");
    }
}