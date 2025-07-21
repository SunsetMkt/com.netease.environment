package com.netease.ntunisdk.unilogger.utils;

import android.os.Environment;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.unilogger.UniLoggerProxy;
import java.io.File;

/* loaded from: classes5.dex */
public class LogUtils {
    public static final String TAG = "UniLogger";
    private static boolean isUniLoggerDebug;
    private static boolean isUnisdkDebug;

    public static void v_inner(String str, String str2) {
        if (isUniLoggerDebug) {
            Log.v(str, str2);
        }
    }

    public static void v_inner(String str) {
        if (isUniLoggerDebug) {
            Log.v(TAG, str);
        }
    }

    public static void d_inner(String str, String str2) {
        if (isUniLoggerDebug) {
            Log.d(str, str2);
        }
    }

    public static void i_inner(String str, String str2) {
        if (isUniLoggerDebug) {
            Log.i(str, str2);
        }
    }

    public static void i_inner(String str) {
        if (isUniLoggerDebug) {
            Log.i(TAG, str);
        }
    }

    public static void w_inner(String str, String str2) {
        if (isUniLoggerDebug) {
            Log.w(str, str2);
        }
    }

    public static void w_inner(String str) {
        if (isUniLoggerDebug) {
            Log.w(TAG, str);
        }
    }

    public static void e_inner(String str, String str2) {
        if (isUniLoggerDebug) {
            Log.e(str, str2);
        }
    }

    public static void e_inner(String str) {
        if (isUniLoggerDebug) {
            Log.e(TAG, str);
        }
    }

    public static void v(String str, String str2) {
        if (isUnisdkDebug) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (isUnisdkDebug) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (isUnisdkDebug) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (isUnisdkDebug) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (isUnisdkDebug) {
            Log.e(str, str2);
        }
    }

    public static void checkDebugEnabled() {
        isUnisdkDebug = false;
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                String str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".data" + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "debug_log";
                File file = new File(str);
                Log.d(TAG, "LogUtils [checkDebugEnabled] debug_log filePath=" + str);
                if (file.exists()) {
                    isUnisdkDebug = true;
                }
                String str2 = UniLoggerProxy.context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "unilogger_log";
                File file2 = new File(str2);
                Log.d(TAG, "LogUtils [checkDebugEnabled] unilogger_log filePath=" + str2);
                if (file2.exists()) {
                    Log.i(com.netease.androidcrashhandler.util.LogUtils.TAG, "LogUtils [containLogFile] exist file, filePath=" + str2);
                    isUniLoggerDebug = true;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "LogUtils [checkDebugEnabled] Exception=" + e.toString());
            e.printStackTrace();
        }
        Log.d(TAG, "LogUtils [checkDebugEnabled] isUnisdkDebug=" + isUnisdkDebug + ", isUniLoggerDebug=" + isUniLoggerDebug);
    }
}