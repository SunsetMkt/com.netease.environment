package com.netease.cloud.nos.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.netease.cloud.nos.android.core.AcceleratorConf;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.monitor.ISendStat;
import com.netease.cloud.nos.android.monitor.Monitor;
import com.netease.cloud.nos.android.monitor.MonitorConfig;
import com.netease.cloud.nos.android.monitor.StatisticItem;
import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes6.dex */
public class MonitorService extends Service {
    private static final String LOGTAG = LogUtil.makeLogTag(MonitorService.class);
    private MsgBinder msgBinder = null;
    private boolean configInit = false;

    public class MsgBinder extends ISendStat.Stub {
        public MsgBinder() {
        }

        @Override // com.netease.cloud.nos.android.monitor.ISendStat
        public void sendConfig(MonitorConfig monitorConfig) throws RemoteException {
            LogUtil.d(MonitorService.LOGTAG, "Receive Monitor config" + monitorConfig.getMonitorHost());
            AcceleratorConf conf = WanAccelerator.getConf();
            conf.setMontiroHost(monitorConfig.getMonitorHost());
            conf.setMonitorInterval(monitorConfig.getMonitorInterval());
            try {
                conf.setConnectionTimeout(monitorConfig.getConnectionTimeout());
                conf.setSoTimeout(monitorConfig.getSoTimeout());
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }
            LogUtil.d(MonitorService.LOGTAG, "current Monitor config" + WanAccelerator.getConf().getMonitorHost());
            MonitorService.this.configInit = true;
        }

        @Override // com.netease.cloud.nos.android.monitor.ISendStat
        public boolean sendStat(StatisticItem statisticItem) throws RemoteException {
            if (Monitor.set(statisticItem)) {
                LogUtil.d(MonitorService.LOGTAG, "send monitor data immediately");
                MonitorService.this.postMonitorData();
            }
            return MonitorService.this.configInit;
        }
    }

    public void postMonitorData() {
        new Thread(new a(this)).start();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LogUtil.d(LOGTAG, "MonitorService onBind");
        return this.msgBinder;
    }

    @Override // android.app.Service
    public void onCreate() {
        LogUtil.d(LOGTAG, "MonitorService onCreate");
        super.onCreate();
        this.msgBinder = new MsgBinder();
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtil.d(LOGTAG, "MonitorService onDestroy");
        this.msgBinder = null;
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i) {
        LogUtil.d(LOGTAG, "Service onStart");
        super.onStart(intent, i);
        postMonitorData();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        LogUtil.d(LOGTAG, "Service onStartCommand");
        return super.onStartCommand(intent, i, i2);
    }
}