package com.netease.download.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.FileHandle;
import com.netease.download.taskManager.TaskManager;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes6.dex */
public class Dispatcher extends Handler {
    private static final String TAG = "Dispatcher";
    private static Dispatcher sDispatcherInstance;
    private static Map<String, FileHandle> sTaskParamsMap = new HashMap();
    private Context mContext;
    public String mSessionId;

    public static Map<String, FileHandle> getTaskParamsMap() {
        return sTaskParamsMap;
    }

    private Dispatcher() {
        super(CommonHandlerThread.getInstance().getLooper());
        this.mSessionId = null;
        if (TextUtils.isEmpty(null)) {
            this.mSessionId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + Util.getFixLenthString(9);
        }
    }

    public static Dispatcher getInstance() {
        if (sDispatcherInstance == null) {
            synchronized (Dispatcher.class) {
                if (sDispatcherInstance == null) {
                    sDispatcherInstance = new Dispatcher();
                }
            }
        }
        return sDispatcherInstance;
    }

    public void start(Context context, DownloadParams downloadParams) {
        LogUtil.i(TAG, "Dispatcher [start]");
        this.mContext = context;
        sendMessage(obtainMessage(1, new Property(context, downloadParams)));
    }

    public void startSyn(Context context, List<DownloadParams> list) {
        LogUtil.i(TAG, "Dispatcher [startSyn]");
        this.mContext = context;
        sendMessage(obtainMessage(11, list));
    }

    public void startPart(Context context, DownloadParams... downloadParamsArr) {
        for (DownloadParams downloadParams : downloadParamsArr) {
            sendMessage(obtainMessage(1, new Property(context, downloadParams)));
        }
    }

    public void stop(DownloadParams... downloadParamsArr) {
        for (DownloadParams downloadParams : downloadParamsArr) {
            sendMessage(obtainMessage(6, downloadParams));
        }
    }

    public void stopTask(List<DownloadParams> list) {
        LogUtil.i(TAG, "\u4e00\u5171\u9700\u8981\u505c\u6b62\u7684\u4e2a\u6570= " + list.size());
        Iterator<DownloadParams> it = list.iterator();
        while (it.hasNext()) {
            sendMessage(obtainMessage(6, it.next()));
        }
    }

    public void restartPaused(boolean z) {
        sendMessage(obtainMessage(8, z ? 1 : 0, 0));
    }

    public void forceFinish() {
        sendEmptyMessageDelayed(9, 5000L);
    }

    public void notifyNetworkChanged() {
        sendEmptyMessage(10);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what != 11) {
            return;
        }
        LogUtil.i(TAG, "Dispatcher [handleMessage]");
        ArrayList arrayList = (ArrayList) message.obj;
        if (arrayList != null) {
            TaskManager.startSynNewTask(this.mContext, arrayList);
        } else {
            LogUtil.i(TAG, "Dispatcher [handleMessage] paramList is null");
        }
    }

    public void close() {
        CommonHandlerThread.getInstance().close();
        sDispatcherInstance = null;
    }

    private static class Property {
        Context context;
        DownloadParams param;

        Property(Context context, DownloadParams downloadParams) {
            this.context = context;
            this.param = downloadParams;
        }
    }
}