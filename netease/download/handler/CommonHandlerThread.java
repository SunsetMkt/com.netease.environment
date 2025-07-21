package com.netease.download.handler;

import android.os.HandlerThread;

/* loaded from: classes6.dex */
public class CommonHandlerThread extends HandlerThread {
    private static final String TAG = "CommonHandlerThread";
    private static CommonHandlerThread sThread;

    private CommonHandlerThread(String str) {
        super(str);
        start();
    }

    public static CommonHandlerThread getInstance() {
        if (sThread == null) {
            synchronized (CommonHandlerThread.class) {
                if (sThread == null) {
                    sThread = new CommonHandlerThread(TAG);
                }
            }
        }
        return sThread;
    }

    public void close() {
        CommonHandlerThread commonHandlerThread = sThread;
        if (commonHandlerThread != null) {
            commonHandlerThread.quit();
            sThread = null;
        }
    }
}