package com.netease.cloud.nos.android.monitor;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.netease.cloud.nos.android.monitor.ISendStat;
import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes5.dex */
final class d implements ServiceConnection {
    d() {
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ISendStat unused = MonitorManager.iSendStat = ISendStat.Stub.asInterface(iBinder);
        LogUtil.d(MonitorManager.LOGTAG, "Stat onServiceConnected, iSendStat=" + MonitorManager.iSendStat);
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        ISendStat unused = MonitorManager.iSendStat = null;
    }
}