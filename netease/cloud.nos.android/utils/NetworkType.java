package com.netease.cloud.nos.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.netease.cloud.nos.android.core.WanAccelerator;

/* loaded from: classes5.dex */
public class NetworkType {
    private int chunkSize;
    private String networkType;

    public NetworkType(String str) {
        this.chunkSize = 32768;
        this.networkType = str;
        this.chunkSize = WanAccelerator.getConf().getChunkSize();
    }

    public NetworkType(String str, int i) {
        this.networkType = str;
        this.chunkSize = i;
    }

    public static NetworkType getFastMobileNetwork(Context context) {
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return new NetworkType("2g", 4096);
            case 3:
            case 5:
            case 6:
                return new NetworkType("3g/4g", 32768);
            case 8:
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
                return new NetworkType("3g/4g", 131072);
            case 10:
                return new NetworkType("3g/4g", 65536);
            default:
                return new NetworkType("2g");
        }
    }

    public static NetworkType getNetWorkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            String typeName = activeNetworkInfo.getTypeName();
            if (typeName.equalsIgnoreCase("WIFI")) {
                return new NetworkType("wifi", 131072);
            }
            if (typeName.equalsIgnoreCase("MOBILE")) {
                return getFastMobileNetwork(context);
            }
        }
        return new NetworkType("");
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public String getNetworkType() {
        return this.networkType;
    }
}