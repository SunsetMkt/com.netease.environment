package com.netease.mpay.ps.codescanner.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.netease.ntunisdk.base.ConstProp;

/* loaded from: classes6.dex */
public class NetworkUtils {
    private static NetworkInfo getNetInfo(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return null;
        }
        return activeNetworkInfo;
    }

    public static boolean isCmwapNet(Context context) {
        NetworkInfo netInfo = getNetInfo(context);
        return netInfo != null && netInfo.getTypeName().equals(ConstProp.NT_AUTH_NAME_MOBILE) && netInfo.getExtraInfo().equals("cmwap");
    }
}