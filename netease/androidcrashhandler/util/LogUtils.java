package com.netease.androidcrashhandler.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import java.io.File;
import java.io.IOException;

/* loaded from: classes4.dex */
public class LogUtils {
    public static final String TAG = "trace";
    private static boolean mContainLogFile = false;
    private static boolean mHasCheckLogFile = false;
    private static boolean mIsDebug = true;

    public static void d(String str, String str2) {
        if (CUniLogger.checkUniLoggerEnv()) {
            CUniLogger.d(str, str2);
        } else if (mIsDebug) {
            Log.d(str, str2);
        }
        if (mContainLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void v(String str, String str2) {
        if (CUniLogger.checkUniLoggerEnv()) {
            CUniLogger.v(str, str2);
        }
        if (mIsDebug) {
            Log.v(str, str2);
        }
        if (mContainLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void i(String str, String str2) {
        if (CUniLogger.checkUniLoggerEnv()) {
            CUniLogger.i(str, str2);
        } else if (mIsDebug) {
            Log.i(str, str2);
        }
        if (mContainLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void w(String str, String str2) {
        if (CUniLogger.checkUniLoggerEnv()) {
            CUniLogger.w(str, str2);
        } else if (mIsDebug) {
            Log.w(str, str2);
        }
        if (mContainLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void e(String str, String str2) {
        if (CUniLogger.checkUniLoggerEnv()) {
            CUniLogger.e(str, str2);
        } else if (mIsDebug) {
            Log.e(str, str2);
        }
        if (mContainLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static boolean checkContainLogFile(Context context) throws IOException {
        Log.i(TAG, "LogUtils [containLogFile] start");
        if (!mHasCheckLogFile && !mContainLogFile) {
            mHasCheckLogFile = true;
            if (context == null) {
                Log.i(TAG, "LogUtils [containLogFile] context is null");
                return false;
            }
            if (context.getExternalFilesDir(null) != null) {
                String str = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + StorageToFileProxy.CRASHHUNTER_LOG_FILE_PATH;
                if (new File(str).exists()) {
                    Log.i(TAG, "LogUtils [containLogFile] \u5b58\u5728log\u6587\u4ef6\uff1a" + str);
                    mContainLogFile = true;
                    StorageToFileProxy.getInstances().init(context, 500, str);
                    StorageToFileProxy.getInstances().start();
                } else {
                    Log.i(TAG, "LogUtils [containLogFile] \u4e0d\u5b58\u5728log\u6587\u4ef6\uff1a" + str);
                }
            }
        }
        return mContainLogFile;
    }

    public static boolean isDebug() {
        return mIsDebug;
    }

    public static void checkDebug() {
        mIsDebug = false;
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".data" + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "debug_log");
                if (file.exists()) {
                    Log.d(TAG, "LogUtils [checkDebug] exist file = " + file.getAbsolutePath());
                    mIsDebug = true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "LogUtils [checkDebug] Exception =" + e.toString());
            e.printStackTrace();
        }
        Log.d(TAG, "LogUtils [checkDebug] mIsDebug =" + mIsDebug);
    }
}