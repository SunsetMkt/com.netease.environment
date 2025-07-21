package com.netease.androidcrashhandler;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class DeviceInfo {
    private static final String TAG = "DeviceInfo";
    private static DeviceInfo sDeviceInfo;

    private DeviceInfo() {
    }

    public static DeviceInfo getInstance() {
        if (sDeviceInfo == null) {
            sDeviceInfo = new DeviceInfo();
        }
        return sDeviceInfo;
    }

    public Map<String, String> getDeviceInfo() {
        return new HashMap();
    }
}