package com.netease.pharos.locationCheck;

import android.content.Context;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.linkcheck.Proxy;
import com.netease.pharos.report.ReportProxy;
import com.netease.pharos.util.LogUtil;

/* loaded from: classes5.dex */
public class LocationCheckProxy {
    private static final String TAG = "LocationCheckProxy";
    private static LocationCheckProxy sLocationCheckProxy;
    private final Context mContext = null;
    private boolean mHasRun = false;

    private LocationCheckProxy() {
    }

    public static LocationCheckProxy getInstances() {
        if (sLocationCheckProxy == null) {
            synchronized (LocationCheckProxy.class) {
                if (sLocationCheckProxy == null) {
                    sLocationCheckProxy = new LocationCheckProxy();
                }
            }
        }
        return sLocationCheckProxy;
    }

    public boolean isHasRun() {
        return this.mHasRun;
    }

    public int start() {
        LogUtil.i(TAG, "LocationCheckProxy [start]");
        NetAreaCore.getInstances().start();
        LocationHunter locationHunter = new LocationHunter();
        DeviceInfo deviceInfoStart = locationHunter.start();
        if (deviceInfoStart != null) {
            locationHunter.checkRegion(deviceInfoStart);
        }
        RecheckResult.getInstance().chooseBest();
        DeviceInfo.getInstance().handleProbeAndHarborRegion();
        String deviceInfo = DeviceInfo.getInstance().getDeviceInfo(false);
        LogUtil.i(TAG, "LocationCheckProxy [start] call report, info=" + deviceInfo);
        ReportProxy.getInstance().report(deviceInfo);
        Proxy.getInstance().setmPharosResultCache(Proxy.getInstance().getPharosResultInfo());
        this.mHasRun = true;
        return 0;
    }

    public void clean() {
        this.mHasRun = false;
        NetAreaInfo.getInstances().clean();
    }
}