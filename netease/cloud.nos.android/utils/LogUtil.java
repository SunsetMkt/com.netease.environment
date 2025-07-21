package com.netease.cloud.nos.android.utils;

import android.util.Log;

/* loaded from: classes5.dex */
public class LogUtil {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static int level = 2;

    private LogUtil() {
    }

    public static int d(String str, String str2) {
        if (3 >= level) {
            return Log.d(str, str2);
        }
        return 0;
    }

    public static int d(String str, String str2, Throwable th) {
        if (3 >= level) {
            return Log.d(str, str2, th);
        }
        return 0;
    }

    public static int e(String str, String str2) {
        if (6 >= level) {
            return Log.e(str, str2);
        }
        return 0;
    }

    public static int e(String str, String str2, Throwable th) {
        if (6 >= level) {
            return Log.e(str, str2, th);
        }
        return 0;
    }

    public static int getLevel() {
        return level;
    }

    public static int i(String str, String str2) {
        if (4 >= level) {
            return Log.i(str, str2);
        }
        return 0;
    }

    public static int i(String str, String str2, Throwable th) {
        if (4 >= level) {
            return Log.i(str, str2, th);
        }
        return 0;
    }

    public static String makeLogTag(Class cls) {
        return "NetEaseNosService_" + cls.getSimpleName();
    }

    public static void setLevel(int i) {
        level = i;
    }

    public static int v(String str, String str2) {
        if (2 >= level) {
            return Log.v(str, str2);
        }
        return 0;
    }

    public static int v(String str, String str2, Throwable th) {
        if (2 >= level) {
            return Log.v(str, str2, th);
        }
        return 0;
    }

    public static int w(String str, String str2) {
        if (5 >= level) {
            return Log.w(str, str2);
        }
        return 0;
    }

    public static int w(String str, String str2, Throwable th) {
        if (5 >= level) {
            return Log.w(str, str2, th);
        }
        return 0;
    }

    public static int w(String str, Throwable th) {
        if (5 >= level) {
            return Log.w(str, th);
        }
        return 0;
    }
}