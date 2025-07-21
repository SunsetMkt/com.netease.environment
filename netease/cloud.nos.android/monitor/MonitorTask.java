package com.netease.cloud.nos.android.monitor;

import android.content.Context;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.utils.LogUtil;
import java.util.TimerTask;

/* loaded from: classes5.dex */
public class MonitorTask extends TimerTask {
    private static final String LOGTAG = LogUtil.makeLogTag(MonitorTask.class);
    private Context ctx;

    public MonitorTask(Context context) {
        this.ctx = context;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() throws Throwable {
        LogUtil.d(LOGTAG, "run MonitorTask");
        MonitorHttp.post(this.ctx, WanAccelerator.getConf().getMonitorHost());
    }
}