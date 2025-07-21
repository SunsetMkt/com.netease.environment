package com.netease.mpay.ps.codescanner.utils;

import android.util.Log;
import com.netease.mpay.ps.codescanner.Configs;

/* loaded from: classes6.dex */
public class Logging {
    private static final String DEBUG_TAG = "mpay-codescanner";
    private static final String LOG_TAG = "MpayCodeScanner";

    private static void log(Object obj) {
    }

    public static void log(String str) {
    }

    public static void logStackTrace(Throwable th) {
    }

    public static void debug(String str) {
        if (Configs.DEBUG_MODE) {
            Log.i(DEBUG_TAG, str);
        }
    }
}