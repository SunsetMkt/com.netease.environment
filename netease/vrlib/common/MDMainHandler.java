package com.netease.vrlib.common;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes5.dex */
public class MDMainHandler {
    private static Handler sMainHandler;

    public static void init() {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
    }

    public static Handler sharedHandler() {
        return sMainHandler;
    }

    public static void release() {
        sMainHandler = null;
    }
}