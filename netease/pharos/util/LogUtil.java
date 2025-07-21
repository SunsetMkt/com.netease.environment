package com.netease.pharos.util;

import android.util.Log;

/* loaded from: classes5.dex */
public class LogUtil {
    private static final String TAG = "pharos";
    private static boolean sIsDebug;

    public static void setIsShowLog(boolean z) {
        sIsDebug = z;
        LogFileProxy.getInstance().setSaveLog(z);
        Log.i(TAG, "pharos sIsDebug = " + sIsDebug);
    }

    public static void v(String str, String str2) {
        if (sIsDebug) {
            Log.v(TAG, str2);
        }
        if (LogFileProxy.getInstance().isSaveLog()) {
            LogFileProxy.getInstance().add(str2 + " \n");
        }
    }

    public static void d(String str, String str2) {
        if (sIsDebug) {
            Log.d(TAG, str2);
        }
        if (LogFileProxy.getInstance().isSaveLog()) {
            LogFileProxy.getInstance().add(str2 + " \n");
        }
    }

    public static void i(String str, String str2) {
        if (sIsDebug) {
            Log.i(TAG, str2);
        }
        if (LogFileProxy.getInstance().isSaveLog()) {
            LogFileProxy.getInstance().add(str2 + " \n");
        }
    }

    public static void w(String str, String str2) {
        if (sIsDebug) {
            Log.w(TAG, str2);
        }
        if (LogFileProxy.getInstance().isSaveLog()) {
            LogFileProxy.getInstance().add(str2 + " \n");
        }
    }

    public static void e(String str, String str2) {
        if (sIsDebug) {
            Log.e(TAG, str2);
        }
        if (LogFileProxy.getInstance().isSaveLog()) {
            LogFileProxy.getInstance().add(str2 + " \n");
        }
    }

    public static void stepLog(String str) {
        if (sIsDebug) {
            Log.i(TAG, "=============================================");
            Log.i(TAG, str);
            Log.i(TAG, "=============================================");
        }
        if (LogFileProxy.getInstance().isSaveLog()) {
            LogFileProxy.getInstance().add(str + " \n");
        }
    }
}