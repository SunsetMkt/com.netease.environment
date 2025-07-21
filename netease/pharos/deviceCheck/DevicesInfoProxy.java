package com.netease.pharos.deviceCheck;

import android.content.Context;
import com.alipay.sdk.m.u.b;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.util.LogUtil;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/* loaded from: classes5.dex */
public class DevicesInfoProxy {
    private static DevicesInfoProxy sDevicesInfoProxy;
    private Context mContext = null;
    private boolean mHasRun = false;

    private DevicesInfoProxy() {
    }

    public static DevicesInfoProxy getInstances() {
        if (sDevicesInfoProxy == null) {
            sDevicesInfoProxy = new DevicesInfoProxy();
        }
        return sDevicesInfoProxy;
    }

    public boolean isHasRun() {
        return this.mHasRun;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public int start() {
        FutureTask futureTask;
        Thread thread;
        LogUtil.i("", "[pharos] start whoami");
        NetDevices.getInstances().init(this.mContext);
        NetDevices.getInstances().start();
        if (PharosProxy.getInstance().isIpv6Verify()) {
            futureTask = new FutureTask(new Callable<Integer>() { // from class: com.netease.pharos.deviceCheck.DevicesInfoProxy.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    return Integer.valueOf(Ipv6Verify.getInstance().start());
                }
            });
            thread = new Thread(futureTask);
            thread.start();
        } else {
            futureTask = null;
            thread = null;
        }
        int iStart = IpInfoCore.getInstances().start();
        if (futureTask != null) {
            try {
                futureTask.get(b.f1465a, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LogUtil.i("", String.format("[pharos] who6 exception", new Object[0]));
                try {
                    thread.interrupt();
                } catch (Exception unused) {
                    LogUtil.i("", String.format("[pharos] who6 interrupt exception", new Object[0]));
                }
                DeviceInfo.getInstance().setIpaddrV6("");
                e.printStackTrace();
            }
        }
        if (iStart != 0) {
            this.mHasRun = false;
        }
        if (iStart == 0) {
            this.mHasRun = true;
        }
        LogUtil.i("", "[pharos] finish whoami");
        return iStart;
    }

    public void clean() {
        this.mHasRun = false;
        DeviceInfo.getInstance().clean();
    }
}