package com.netease.ntunisdk.lbs;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

/* loaded from: classes.dex */
public class LegacyPermissionUtil {
    public static int checkSelfPermission(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ((Activity) context).checkSelfPermission(str);
        }
        return 0;
    }

    public static boolean shouldShowRequestPermissionRationale(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ((Activity) context).shouldShowRequestPermissionRationale(str);
        }
        return false;
    }
}