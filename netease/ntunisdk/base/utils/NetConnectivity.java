package com.netease.ntunisdk.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.api.ModulesManager;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class NetConnectivity {
    private static final String TAG = "NetConnectivity";

    public static boolean isConnectionFast(int i, int i2) {
        if (i == 1) {
            return true;
        }
        if (i != 0) {
            return false;
        }
        switch (i2) {
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 15:
                return true;
            case 4:
            case 7:
            case 11:
            default:
                return false;
        }
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                return connectivityManager.getActiveNetworkInfo();
            }
            return null;
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "NetConnectivity [getNetworkInfo] Exception=".concat(String.valueOf(e)));
            return null;
        }
    }

    public static JSONObject getNetworkInfo2(Context context) {
        try {
            String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getNetworkInfoJson\", \"from\":\"NetConnectivity\"}");
            UniSdkUtils.d(TAG, "res = ".concat(String.valueOf(strExtendFunc)));
            return new JSONObject(strExtendFunc);
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "NetConnectivity [getNetworkInfo] Exception=".concat(String.valueOf(e)));
            return null;
        }
    }

    public static boolean isConnected(Context context) {
        JSONObject networkInfo2 = getNetworkInfo2(context);
        return networkInfo2 != null && networkInfo2.optBoolean("isConnected");
    }

    public static boolean isConnectedWifi(Context context) {
        JSONObject networkInfo2 = getNetworkInfo2(context);
        return networkInfo2 != null && networkInfo2.optBoolean("isConnected") && networkInfo2.optInt("getType", -1) == 1;
    }

    public static boolean isConnectedMobile(Context context) {
        JSONObject networkInfo2 = getNetworkInfo2(context);
        return networkInfo2 != null && networkInfo2.optBoolean("isConnected") && networkInfo2.optInt("getType", -1) == 0;
    }

    public static boolean isConnectedFast(Context context) {
        JSONObject networkInfo2 = getNetworkInfo2(context);
        return networkInfo2 != null && networkInfo2.optBoolean("isConnected") && isConnectionFast(networkInfo2.optInt("getType", -1), networkInfo2.optInt("getSubtype", -1));
    }

    public static String getNetworkType(Context context) {
        if (SdkMgr.getInst() == null) {
            ModulesManager.getInst().init(context);
        }
        return ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getNetworkType2\"}");
    }
}