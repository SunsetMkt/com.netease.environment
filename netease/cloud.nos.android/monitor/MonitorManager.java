package com.netease.cloud.nos.android.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.service.MonitorService;
import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes5.dex */
public class MonitorManager {
    private Context ctx;
    private StatisticItem item;
    private static final String LOGTAG = LogUtil.makeLogTag(MonitorManager.class);
    private static boolean monitorConfigInit = false;
    private static boolean running = false;
    private static int refCount = 0;
    private static ISendStat iSendStat = null;
    private static ServiceConnection conn = new d();
    private ISendStat instSendStat = null;
    private ServiceConnection instConn = new c(this);

    public MonitorManager(Context context, StatisticItem statisticItem) {
        this.ctx = null;
        this.item = null;
        this.ctx = context;
        this.item = statisticItem;
    }

    public static synchronized void endService(Context context) {
        int i = refCount;
        if (i != 0) {
            refCount = i - 1;
            if (i <= 1) {
                context.getApplicationContext().unbindService(conn);
                LogUtil.d(LOGTAG, "unbind MonitorService success");
                return;
            }
        }
        LogUtil.d(LOGTAG, "MonitorService has binded to else or unbinded: refCount=" + refCount);
    }

    private static synchronized void runService(Context context) {
        if (running) {
            return;
        }
        running = true;
        LogUtil.d(LOGTAG, "init MonitorService");
        context.startService(new Intent(context, (Class<?>) MonitorService.class));
    }

    public static void sendStatItem(Context context, StatisticItem statisticItem) {
        ISendStat iSendStat2 = iSendStat;
        if (iSendStat2 == null) {
            LogUtil.d(LOGTAG, "iSendStat is null, bind to MonitorService");
            runService(context);
            new MonitorManager(context, statisticItem).instStartService();
            return;
        }
        try {
            iSendStat2.sendStat(statisticItem);
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "send Statistic data exception: " + e.getMessage() + "iSendStat=" + iSendStat);
            e.printStackTrace();
        }
    }

    public static synchronized void startService(Context context) {
        int i = refCount;
        refCount = i + 1;
        if (i > 0) {
            LogUtil.d(LOGTAG, "MonitorService has binded: refCount=" + refCount);
            return;
        }
        if (iSendStat != null) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        applicationContext.bindService(new Intent(applicationContext, (Class<?>) MonitorService.class), conn, 1);
        LogUtil.d(LOGTAG, "bind MonitorService, iSendStat=" + iSendStat);
    }

    public void instEndService() {
        this.ctx.unbindService(this.instConn);
        LogUtil.d(LOGTAG, "unbind MonitorService success");
    }

    public void instSendConfig() {
        if (this.instSendStat == null) {
            LogUtil.w(LOGTAG, "instSendStat is null, not bind to MonitorService");
            return;
        }
        if (monitorConfigInit) {
            return;
        }
        try {
            this.instSendStat.sendConfig(new MonitorConfig(WanAccelerator.getConf().getMonitorHost(), WanAccelerator.getConf().getConnectionTimeout(), WanAccelerator.getConf().getSoTimeout(), WanAccelerator.getConf().getMonitorInterval()));
            LogUtil.d(LOGTAG, "send config to MonitorService");
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "send MonitorConfig exception: " + e.getMessage() + "instSendStat=" + this.instSendStat);
            e.printStackTrace();
        }
    }

    public void instSendStatItem() {
        ISendStat iSendStat2 = this.instSendStat;
        if (iSendStat2 == null) {
            LogUtil.w(LOGTAG, "instSendStat is null, not bind to MonitorService");
            return;
        }
        try {
            monitorConfigInit = iSendStat2.sendStat(this.item);
            LogUtil.d(LOGTAG, "send statistic to MonitorService, get configInit " + monitorConfigInit);
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "send Statistic data exception: " + e.getMessage() + "instSendStat=" + this.instSendStat);
            e.printStackTrace();
        }
    }

    public void instStartService() {
        if (this.instSendStat != null) {
            return;
        }
        this.ctx.bindService(new Intent(this.ctx, (Class<?>) MonitorService.class), this.instConn, 1);
        LogUtil.d(LOGTAG, "bind MonitorService, instSendStat=" + this.instSendStat);
    }
}