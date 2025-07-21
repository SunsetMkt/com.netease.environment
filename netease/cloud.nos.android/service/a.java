package com.netease.cloud.nos.android.service;

import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.monitor.MonitorHttp;
import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes6.dex */
class a implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ MonitorService f1547a;

    a(MonitorService monitorService) {
        this.f1547a = monitorService;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        LogUtil.d(MonitorService.LOGTAG, "postMonitorData to host " + WanAccelerator.getConf().getMonitorHost());
        MonitorHttp.post(this.f1547a, WanAccelerator.getConf().getMonitorHost());
    }
}