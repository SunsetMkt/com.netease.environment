package com.netease.androidcrashhandler;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.os.Build;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* loaded from: classes2.dex */
public class MyTombstone {
    private static final String TAG = "DeviceInfo";
    private static MyTombstone sMyTombstone;

    private MyTombstone() {
    }

    public static MyTombstone getInstance() {
        if (sMyTombstone == null) {
            sMyTombstone = new MyTombstone();
        }
        return sMyTombstone;
    }

    public String getLastTimeCrashTombstone(Context context, String str, int i, long j) {
        return Build.VERSION.SDK_INT >= 31 ? getLastTimeExitReasonFile(context, new File(str, System.currentTimeMillis() + ".tombstone").getAbsolutePath(), i, j, 5) : "";
    }

    public String getLastTimeAnrFile(Context context, String str, int i) {
        return Build.VERSION.SDK_INT >= 30 ? getLastTimeExitReasonFile(context, new File(str, System.currentTimeMillis() + Const.FileNameTag.TRACE_FILE).getAbsolutePath(), i, 0L, 6) : "";
    }

    private String getLastTimeExitReasonFile(Context context, String str, int i, long j, int i2) {
        List<ApplicationExitInfo> historicalProcessExitReasons;
        FileOutputStream fileOutputStream;
        try {
            if (Build.VERSION.SDK_INT >= 30 && context != null) {
                LogUtils.d(LogUtils.TAG, "[getLastTimeCrashTombstone] start pid:" + i + " time:" + j);
                ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
                InputStream inputStream = null;
                if (i > 0) {
                    historicalProcessExitReasons = activityManager.getHistoricalProcessExitReasons(null, i, 0);
                } else {
                    historicalProcessExitReasons = activityManager.getHistoricalProcessExitReasons(context.getPackageName(), 0, 0);
                }
                LogUtils.d(LogUtils.TAG, "[getLastTimeCrashTombstone] list:" + historicalProcessExitReasons.size());
                for (int i3 = 0; i3 < historicalProcessExitReasons.size(); i3++) {
                    ApplicationExitInfo applicationExitInfo = historicalProcessExitReasons.get(i3);
                    LogUtils.d(LogUtils.TAG, "[getLastTimeCrashTombstone] " + applicationExitInfo.getReason() + "_" + historicalProcessExitReasons.get(i3).toString());
                    if (applicationExitInfo.getReason() == i2) {
                        if (j > 0 && Math.abs(applicationExitInfo.getTimestamp() - j) > 30000) {
                            return "";
                        }
                        LogUtils.d(LogUtils.TAG, "[getLastTimeCrashTombstone] match");
                        File file = new File(str);
                        try {
                            InputStream traceInputStream = applicationExitInfo.getTraceInputStream();
                            try {
                                byte[] bArr = new byte[1024];
                                fileOutputStream = new FileOutputStream(file);
                                while (true) {
                                    try {
                                        int i4 = traceInputStream.read(bArr);
                                        if (i4 <= 0) {
                                            break;
                                        }
                                        fileOutputStream.write(bArr, 0, i4);
                                    } catch (Throwable th) {
                                        th = th;
                                        inputStream = traceInputStream;
                                        try {
                                            th.printStackTrace();
                                            file.delete();
                                            LogUtils.d(LogUtils.TAG, "[getLastTimeCrashTombstone] get tombstone fail");
                                            if (inputStream != null) {
                                                try {
                                                    inputStream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (fileOutputStream != null) {
                                                try {
                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();
                                                } catch (IOException e2) {
                                                    e2.printStackTrace();
                                                }
                                            }
                                            return "";
                                        } finally {
                                        }
                                    }
                                }
                                if (traceInputStream != null) {
                                    try {
                                        traceInputStream.close();
                                    } catch (IOException e3) {
                                        e3.printStackTrace();
                                    }
                                }
                                try {
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                                LogUtils.d(LogUtils.TAG, "[getLastTimeCrashTombstone] copy finish:" + file.getAbsolutePath());
                                return file.getName();
                            } catch (Throwable th2) {
                                th = th2;
                                fileOutputStream = null;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = null;
                        }
                    }
                }
            }
        } catch (Throwable th4) {
            th4.printStackTrace();
        }
        return "";
    }
}