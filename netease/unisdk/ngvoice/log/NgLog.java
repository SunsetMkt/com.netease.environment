package com.netease.unisdk.ngvoice.log;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import java.io.File;

/* loaded from: classes6.dex */
public final class NgLog {
    public static final String NT_UNISDK_DEBUG_KEY = "NtUniSdkDebug_key";
    private static boolean isDebug;

    public static void checkIsDebug(Context context) {
        isDebug = isDebug(context);
        Log.d("ng_voice", "NgLog log:" + isDebug);
    }

    public static void v(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.v(str, createMsg(str2, objArr));
        }
    }

    public static void d(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.d(str, createMsg(str2, objArr));
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.i(str, createMsg(str2, objArr));
        }
    }

    public static void w(String str, String str2, Object... objArr) {
        if (isDebug) {
            Log.w(str, createMsg(str2, objArr));
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        Log.e(str, createMsg(str2, objArr));
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
        if (isDebug) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
    }

    private static String createMsg(String str, Object... objArr) {
        return objArr.length == 0 ? str : String.format(str, objArr);
    }

    private static boolean isDebug(Context context) throws IllegalAccessException, IllegalArgumentException {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".data" + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "debug_log").exists()) {
                    return true;
                }
            }
        } catch (Exception unused) {
        }
        try {
            boolean z = Class.forName(context.getPackageName() + ".BuildConfig").getDeclaredField("DEBUG").getBoolean(null);
            if (z) {
                return z;
            }
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NullPointerException unused2) {
        }
        try {
            return 1 == Settings.System.getInt(context.getContentResolver(), NT_UNISDK_DEBUG_KEY);
        } catch (Settings.SettingNotFoundException unused3) {
            return false;
        }
    }
}