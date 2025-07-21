package com.netease.androidcrashhandler.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.lmy.smartrefreshlayout.SpinnerStyleConstants;
import com.xiaomi.onetrack.b.a;

/* loaded from: classes4.dex */
public class BatteryUtil {
    public static double getBatteryLevelInfo(Context context) {
        try {
            Intent intentRegisterReceiver = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            return intentRegisterReceiver.getIntExtra(a.d, 0) / intentRegisterReceiver.getIntExtra(SpinnerStyleConstants.SCALE, 1);
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "Device [getBatteryLevelInfo] Exception=" + e.toString());
            e.printStackTrace();
            return 0.0d;
        }
    }

    public static boolean getBatteryIsCharge(Context context) {
        try {
            int intExtra = context.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("status", -1);
            return intExtra == 2 || intExtra == 5;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}