package com.netease.push.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import com.facebook.common.callercontext.ContextChain;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes3.dex */
public class PushLog {
    private static final String NtUniSdkDebug_key = "NtUniSdkDebug_key";
    private static final String TAG = "NGPush_" + PushLog.class.getSimpleName();
    private static Class<?> s_clazzImpl = null;
    private static boolean s_bDebug = false;
    private static boolean s_bInit = false;

    private void patchPlaceholder() {
        Log.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static void init(Context context) {
        if (s_bInit) {
            return;
        }
        s_bInit = true;
        Log.i(TAG, "init");
        try {
            s_clazzImpl = Class.forName("com.netease.ntunisdk.base.UniSdkUtils");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            s_clazzImpl = null;
        }
        s_bDebug = isDebug(context);
        Log.e(TAG, "s_clazzImpl:" + s_clazzImpl + ", s_bDebug:" + s_bDebug);
    }

    private static boolean isDebug(Context context) {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".data" + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "debug_log").exists()) {
                    return true;
                }
            }
            return 1 == Settings.System.getInt(context.getContentResolver(), "NtUniSdkDebug_key");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public static void v(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_bDebug) {
            Log.v(str, str2);
            return;
        }
        Class<?> cls = s_clazzImpl;
        if (cls == null) {
            return;
        }
        try {
            cls.getMethod("v", String.class, String.class).invoke(null, str, str2);
        } catch (Exception unused) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_bDebug) {
            Log.d(str, str2);
            return;
        }
        Class<?> cls = s_clazzImpl;
        if (cls == null) {
            return;
        }
        try {
            cls.getMethod("d", String.class, String.class).invoke(null, str, str2);
        } catch (Exception unused) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_bDebug) {
            Log.i(str, str2);
            return;
        }
        Class<?> cls = s_clazzImpl;
        if (cls == null) {
            return;
        }
        try {
            cls.getMethod(ContextChain.TAG_INFRA, String.class, String.class).invoke(null, str, str2);
        } catch (Exception unused) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_bDebug) {
            Log.w(str, str2);
            return;
        }
        Class<?> cls = s_clazzImpl;
        if (cls == null) {
            return;
        }
        try {
            cls.getMethod("w", String.class, String.class).invoke(null, str, str2);
        } catch (Exception unused) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_bDebug) {
            Log.e(str, str2);
            return;
        }
        Class<?> cls = s_clazzImpl;
        if (cls == null) {
            return;
        }
        try {
            cls.getMethod(RsaJsonWebKey.EXPONENT_MEMBER_NAME, String.class, String.class).invoke(null, str, str2);
        } catch (Exception unused) {
            Log.e(str, str2);
        }
    }
}