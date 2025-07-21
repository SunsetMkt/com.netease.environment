package com.netease.ntunisdk.modules.base.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import com.netease.unisdk.ngvoice.log.NgLog;
import java.io.File;

/* loaded from: classes.dex */
public class LogModule {
    private static final String TAG = "UniSDK LogModule";
    public static boolean isDebug;

    public static void checkIsDebug(Context context) {
        isDebug = isDebug(context);
        Log.d(TAG, "checkIsDebug:" + isDebug);
    }

    public static void v(String str, String str2) {
        if (isDebug) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (isDebug) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (isDebug) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        Log.w(str, str2);
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
    }

    private static boolean isDebug(Context context) {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".data" + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "debug_log").exists()) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        try {
            if (1 == Settings.System.getInt(context.getContentResolver(), NgLog.NT_UNISDK_DEBUG_KEY)) {
                return true;
            }
        } catch (Settings.SettingNotFoundException unused2) {
        }
        try {
            return Class.forName(context.getPackageName() + ".BuildConfig").getDeclaredField("DEBUG").getBoolean(null);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NullPointerException unused3) {
            return false;
        }
    }
}