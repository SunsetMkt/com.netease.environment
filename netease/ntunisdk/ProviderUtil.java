package com.netease.ntunisdk;

import android.content.Context;
import android.location.LocationManager;
import com.netease.ntunisdk.base.UniSdkUtils;

/* loaded from: classes.dex */
class ProviderUtil {
    private static final String TAG = "ProviderUtil";

    ProviderUtil() {
    }

    static boolean isGpsEnable(Context context) {
        return isProviderEnable(context, "gps");
    }

    static boolean isNetworkEnable(Context context) {
        return isProviderEnable(context, "network");
    }

    static boolean isProviderEnable(Context context, String str) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            if (locationManager != null) {
                return locationManager.isProviderEnabled(str);
            }
            return false;
        } catch (Exception e) {
            UniSdkUtils.w(TAG, "check provider error: " + e);
            e.printStackTrace();
            return false;
        }
    }
}