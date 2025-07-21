package com.netease.androidcrashhandler.thirdparty.unilogger;

import android.content.Context;
import android.util.Log;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.unilogger.UniLogger;
import com.netease.ntunisdk.unilogger.UniLoggerProxy;

/* loaded from: classes6.dex */
public class CUniLogger {
    public static final String source = "crashhunter";
    private static UniLogger uniLogger;

    public static void createNewUniLoggerInstance(Context context) {
        Log.i(LogUtils.TAG, "init UniLogger");
        if (uniLogger == null) {
            uniLogger = UniLoggerProxy.getInstance().createNewUniLoggerInstance(context, source);
        }
    }

    public static boolean checkUniLoggerEnv() {
        return uniLogger != null;
    }

    public static void v(String str, String str2) {
        if (checkUniLoggerEnv()) {
            uniLogger.v(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (checkUniLoggerEnv()) {
            uniLogger.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (checkUniLoggerEnv()) {
            uniLogger.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (checkUniLoggerEnv()) {
            uniLogger.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (checkUniLoggerEnv()) {
            uniLogger.e(str, str2);
        }
    }

    public static void setUdid(String str) {
        if (checkUniLoggerEnv()) {
            uniLogger.setUdid(str);
        }
    }

    public static void setUid(String str) {
        if (checkUniLoggerEnv()) {
            uniLogger.setUid(str);
        }
    }

    public static void setRoleId(String str) {
        if (checkUniLoggerEnv()) {
            uniLogger.setRoleId(str);
        }
    }

    public static void setGameid(String str) {
        if (checkUniLoggerEnv()) {
            uniLogger.setGameid(str);
        }
    }
}