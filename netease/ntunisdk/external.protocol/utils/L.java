package com.netease.ntunisdk.external.protocol.utils;

import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public class L {
    private static final String LOG_TAG = "UniP#";
    private static boolean isDebug;

    public static void setDebug(boolean z) {
        isDebug = z;
    }

    public static void d(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.d(LOG_TAG + str, createMsg(str2, objArr));
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.w(LOG_TAG + str, createMsg(str2, objArr));
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.e(LOG_TAG + str, createMsg(str2, objArr));
        }
    }

    public static void d(String str, String str2) {
        if (!TextUtils.isEmpty(str2) && isDebug) {
            Log.d(LOG_TAG + str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        Log.d(LOG_TAG + str, str2);
    }

    public static void w(String str, String str2) {
        if (!TextUtils.isEmpty(str2) && isDebug) {
            Log.w(LOG_TAG + str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (!TextUtils.isEmpty(str2) && isDebug) {
            Log.e(LOG_TAG + str, str2);
        }
    }

    public static void d(String str) {
        if (!TextUtils.isEmpty(str) && isDebug) {
            Log.d(LOG_TAG, str);
        }
    }

    public static void w(String str) {
        if (!TextUtils.isEmpty(str) && isDebug) {
            Log.w(LOG_TAG, str);
        }
    }

    public static void e(String str) {
        if (!TextUtils.isEmpty(str) && isDebug) {
            Log.e(LOG_TAG, str);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(LOG_TAG + str, str2, th);
    }

    private static String createMsg(String str, Object... objArr) {
        return objArr.length == 0 ? str : String.format(str, objArr);
    }
}