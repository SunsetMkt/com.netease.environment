package com.netease.androidcrashhandler.util;

import android.util.Log;
import com.netease.androidcrashhandler.thirdparty.deviceInfoModule.DeviceInfoProxy;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class WifiUtil {
    public static boolean isConnectedWifi() {
        boolean z = false;
        try {
            JSONObject networkInfo = DeviceInfoProxy.getNetworkInfo();
            if (networkInfo != null && networkInfo.has("isConnected") && networkInfo.has("getType")) {
                boolean zOptBoolean = networkInfo.optBoolean("isConnected");
                int iOptInt = networkInfo.optInt("getType");
                if (zOptBoolean && iOptInt == 1) {
                    z = true;
                }
                LogUtils.i(LogUtils.TAG, "WifiUtil [isConnectedWifi] isConnected=" + zOptBoolean + ", type=" + iOptInt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "WifiUtil [isConnectedWifi] Exception=" + e.toString());
        }
        return z;
    }

    public static boolean isConnectedMobile() {
        boolean z = false;
        try {
            JSONObject networkInfo = DeviceInfoProxy.getNetworkInfo();
            if (networkInfo != null && networkInfo.has("isConnected") && networkInfo.has("getType")) {
                boolean zOptBoolean = networkInfo.optBoolean("isConnected");
                int iOptInt = networkInfo.optInt("getType");
                if (zOptBoolean && iOptInt == 0) {
                    z = true;
                }
                LogUtils.i(LogUtils.TAG, "WifiUtil [isConnectedMobile] isConnected=" + zOptBoolean + ", type=" + iOptInt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "WifiUtil [isConnectedMobile] Exception=" + e.toString());
        }
        return z;
    }

    public static boolean isConnectNet() {
        boolean zOptBoolean = false;
        try {
            JSONObject networkInfo = DeviceInfoProxy.getNetworkInfo();
            if (networkInfo == null || !networkInfo.has("isConnected")) {
                return false;
            }
            zOptBoolean = networkInfo.optBoolean("isConnected");
            LogUtils.i(LogUtils.TAG, "WifiUtil [isConnectNet] isConnected=" + zOptBoolean);
            return zOptBoolean;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "WifiUtil [isConnectNet] Exception=" + e.toString());
            return zOptBoolean;
        }
    }
}