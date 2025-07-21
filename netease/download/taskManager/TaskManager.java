package com.netease.download.taskManager;

import android.content.Context;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloadpart.DownloadAllProxy;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class TaskManager {
    private static final String TAG = "TaskManager";

    public static void startSynNewTask(Context context, ArrayList<DownloadParams> arrayList) {
        LogUtil.i(TAG, "startSynNewTask");
        DownloadAllProxy.getInstances().init(arrayList);
        DownloadAllProxy.getInstances().start();
    }
}