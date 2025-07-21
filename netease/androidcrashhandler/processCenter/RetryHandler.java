package com.netease.androidcrashhandler.processCenter;

import android.os.Handler;
import android.os.HandlerThread;
import com.netease.androidcrashhandler.util.LogUtils;

/* loaded from: classes5.dex */
public class RetryHandler extends HandlerThread {
    private static RetryHandler HANDLER = new RetryHandler();
    private Handler mHandler;

    public interface RetryTask {
        void run();
    }

    public static RetryHandler getInstance() {
        return HANDLER;
    }

    private RetryHandler() {
        super("RetryHandler");
        start();
        this.mHandler = new Handler(getLooper());
    }

    public void sendRetryTaskDelay(final RetryTask retryTask, long j) {
        LogUtils.i(LogUtils.TAG, "RetryHandler [sendRetryTaskDelay] send to retryTask");
        this.mHandler.postDelayed(new Runnable() { // from class: com.netease.androidcrashhandler.processCenter.RetryHandler.1
            @Override // java.lang.Runnable
            public void run() {
                retryTask.run();
            }
        }, j);
    }
}