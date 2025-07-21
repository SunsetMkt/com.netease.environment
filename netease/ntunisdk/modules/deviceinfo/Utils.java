package com.netease.ntunisdk.modules.deviceinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import com.netease.ntunisdk.modules.base.utils.LogModule;

/* loaded from: classes.dex */
class Utils {
    public static final String KEY_FIRST_DEVICE_ID_CACHED = "first_deviceId_cached";
    private static final String TAG = "UNISDK DeviceUtils";
    private static SharedPreferences sharedPreferences;

    Utils() {
    }

    static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    static boolean checkSelfPermission(Context context, String str) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        try {
            return context.checkSelfPermission(str) == 0;
        } catch (Exception unused) {
            return false;
        }
    }

    static int getTargetSdkVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            LogModule.d(TAG, "getTargetSdkVersion: " + e.getMessage());
            return 0;
        } catch (Throwable th) {
            LogModule.d(TAG, "getTargetSdkVersion: " + th.getMessage());
            return 0;
        }
    }

    public static void spCache(Context context, String str, String str2) {
        if (context == null) {
            return;
        }
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(TAG, 0);
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    public static String spGet(Context context, String str) {
        if (context == null) {
            return null;
        }
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(TAG, 0);
        }
        return sharedPreferences.getString(str, null);
    }
}