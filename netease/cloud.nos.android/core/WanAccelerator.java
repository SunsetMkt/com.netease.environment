package com.netease.cloud.nos.android.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.core.app.NotificationCompat;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.monitor.MonitorTask;
import com.netease.cloud.nos.android.service.MonitorService;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes5.dex */
public class WanAccelerator {
    private static AcceleratorConf conf;
    private static boolean isInit;
    protected static boolean isOpened;
    private static Timer monitorTimer;
    private static final String LOGTAG = LogUtil.makeLogTag(WanAccelerator.class);
    public static Map<String, String> map = new ConcurrentHashMap();

    public static AcceleratorConf getConf() {
        if (conf == null) {
            conf = new AcceleratorConf();
        }
        return conf;
    }

    private static void initScheduler(Context context) {
        if (!getConf().isMonitorThreadEnabled()) {
            LogUtil.d(LOGTAG, "init scheduler");
            ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).setRepeating(1, 0L, getConf().getMonitorInterval(), PendingIntent.getService(context, 0, new Intent(context, (Class<?>) MonitorService.class), 0));
        } else {
            LogUtil.d(LOGTAG, "init monitor timer");
            monitorTimer = new Timer();
            monitorTimer.schedule(new MonitorTask(context), getConf().getMonitorInterval(), getConf().getMonitorInterval());
        }
    }

    private static UploadTaskExecutor put(Context context, String str, String str2, String str3, File file, Object obj, String str4, Callback callback, boolean z, WanNOSObject wanNOSObject) {
        if (!isInit) {
            isInit = true;
            initScheduler(context);
        }
        try {
            UploadTask uploadTask = new UploadTask(context, str, str2, str3, file, obj, str4, callback, z, wanNOSObject);
            uploadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
            return new UploadTaskExecutor(uploadTask);
        } catch (Exception e) {
            callback.onFailure(new CallRet(obj, str4, 999, "", "", null, e));
            return null;
        }
    }

    public static UploadTaskExecutor putFileByHttp(Context context, File file, Object obj, String str, WanNOSObject wanNOSObject, Callback callback) throws InvalidParameterException {
        Util.checkParameters(context, file, obj, wanNOSObject, callback);
        return put(context, wanNOSObject.getUploadToken(), wanNOSObject.getNosBucketName(), wanNOSObject.getNosObjectName(), file, obj, str, callback, false, wanNOSObject);
    }

    public static UploadTaskExecutor putFileByHttps(Context context, File file, Object obj, String str, WanNOSObject wanNOSObject, Callback callback) throws InvalidParameterException {
        Util.checkParameters(context, file, obj, wanNOSObject, callback);
        return put(context, wanNOSObject.getUploadToken(), wanNOSObject.getNosBucketName(), wanNOSObject.getNosObjectName(), file, obj, str, callback, true, wanNOSObject);
    }

    public static void setConf(AcceleratorConf acceleratorConf) {
        conf = acceleratorConf;
    }
}