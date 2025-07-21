package com.netease.download.util;

import android.util.Log;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.TaskHandle;
import com.netease.download.storage.StorageToFileProxy;
import java.io.File;

/* loaded from: classes5.dex */
public class LogUtil {
    private static final String TAG = "Downloader";
    private static boolean mContainLogFile = false;
    public static boolean mIsShowLog = true;

    public static void v(String str, String str2) {
        if (mIsShowLog) {
            Log.v(TAG, str2);
        }
        if (TaskHandle.sWriteToLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void d(String str, String str2) {
        if (mIsShowLog) {
            Log.d(TAG, str2);
        }
        if (TaskHandle.sWriteToLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void i(String str, String str2) {
        if (mIsShowLog) {
            Log.i(TAG, str2);
        }
        if (TaskHandle.sWriteToLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void w(String str, String str2) {
        if (mIsShowLog) {
            Log.w(TAG, str2);
        }
        if (TaskHandle.sWriteToLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static void e(String str, String str2) {
        if (mIsShowLog) {
            Log.e(TAG, str2);
        }
        if (TaskHandle.sWriteToLogFile) {
            StorageToFileProxy.getInstances().add(str2 + " \n");
        }
    }

    public static boolean IsShowLog() {
        return mIsShowLog;
    }

    public static void setIsShowLog(boolean z) {
        mIsShowLog = z;
    }

    public static boolean containLogFile() {
        File externalFilesDir;
        Log.d(TAG, "LogUtils [containLogFile] start");
        if (!mContainLogFile && DownloadProxy.mContext != null && (externalFilesDir = DownloadProxy.mContext.getExternalFilesDir(null)) != null && externalFilesDir.exists()) {
            File file = new File(externalFilesDir.getAbsolutePath() + "/android_download_log/download_result.txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                Log.d(TAG, "LogUtils mContainLogFile true");
                mContainLogFile = true;
                StorageToFileProxy instances = StorageToFileProxy.getInstances();
                DownloadProxy.getInstance();
                instances.init(DownloadProxy.mContext);
                StorageToFileProxy.getInstances().start();
            }
        }
        Log.d(TAG, "LogUtils [containLogFile] mContainLogFile=" + mContainLogFile);
        return mContainLogFile;
    }

    public static void stepLog(String str) {
        if (mIsShowLog) {
            Log.i(TAG, "=============================================");
            Log.i(TAG, str);
            Log.i(TAG, "=============================================");
        }
    }
}