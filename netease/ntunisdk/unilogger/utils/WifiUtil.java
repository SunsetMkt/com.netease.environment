package com.netease.ntunisdk.unilogger.utils;

import android.util.Log;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.ntunisdk.modules.api.ModulesManager;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class WifiUtil {
    public static JSONObject getNetworkInfo() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getNetworkInfoJson");
            jSONObject.put("from", "NetConnectivity");
            String strExtendFunc = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            LogUtils.i(com.netease.androidcrashhandler.util.LogUtils.TAG, "WifiUtil [getNetworkInfo] networkInfoJson=" + strExtendFunc);
            return new JSONObject(strExtendFunc);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(com.netease.androidcrashhandler.util.LogUtils.TAG, "WifiUtil [getNetworkInfo] Exception=" + e.toString());
            return null;
        }
    }

    public static boolean isConnectedWifi() {
        boolean z = false;
        try {
            JSONObject networkInfo = getNetworkInfo();
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
            Log.i(com.netease.androidcrashhandler.util.LogUtils.TAG, "WifiUtil [isConnectedWifi] Exception=" + e.toString());
        }
        return z;
    }

    public static boolean isConnectedMobile() {
        boolean z = false;
        try {
            JSONObject networkInfo = getNetworkInfo();
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
            Log.i(com.netease.androidcrashhandler.util.LogUtils.TAG, "WifiUtil [isConnectedMobile] Exception=" + e.toString());
        }
        return z;
    }

    public static boolean isConnectNet() {
        boolean zOptBoolean = false;
        try {
            JSONObject networkInfo = getNetworkInfo();
            if (networkInfo == null || !networkInfo.has("isConnected")) {
                return false;
            }
            zOptBoolean = networkInfo.optBoolean("isConnected");
            LogUtils.i(LogUtils.TAG, "WifiUtil [isConnectNet] isConnected=" + zOptBoolean);
            return zOptBoolean;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(com.netease.androidcrashhandler.util.LogUtils.TAG, "WifiUtil [isConnectNet] Exception=" + e.toString());
            return zOptBoolean;
        }
    }
}