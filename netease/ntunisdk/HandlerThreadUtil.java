package com.netease.ntunisdk;

import android.os.HandlerThread;

/* loaded from: classes.dex */
public class HandlerThreadUtil {
    private static final String TAG = "HandlerThreadUtil";
    private static HandlerThread mThread;

    private HandlerThreadUtil() {
    }

    public static HandlerThread getWorkerThread() {
        if (mThread == null) {
            synchronized (HandlerThreadUtil.class) {
                if (mThread == null) {
                    mThread = new HandlerThread(TAG);
                    mThread.start();
                }
            }
        }
        return mThread;
    }
}