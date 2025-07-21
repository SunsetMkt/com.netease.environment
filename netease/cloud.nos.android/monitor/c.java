package com.netease.cloud.nos.android.monitor;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.netease.cloud.nos.android.monitor.ISendStat;
import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes5.dex */
class c implements ServiceConnection {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ MonitorManager f1546a;

    c(MonitorManager monitorManager) {
        this.f1546a = monitorManager;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.f1546a.instSendStat = ISendStat.Stub.asInterface(iBinder);
        LogUtil.d(MonitorManager.LOGTAG, "Stat onServiceConnected, instSendStat=" + this.f1546a.instSendStat);
        this.f1546a.instSendConfig();
        this.f1546a.instSendStatItem();
        this.f1546a.instEndService();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.f1546a.instSendStat = null;
    }
}