package com.netease.push.utils;

import android.content.Context;
import android.os.Build;
import com.netease.inner.pushclient.honor.Honor;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes3.dex */
public class DeviceInfo {
    private static final String TAG = "NGPush_" + DeviceInfo.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static boolean isMIUI(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "Build.MODEL:" + Build.MODEL);
        PushLog.d(TAG, "Build.BRAND:" + Build.BRAND);
        PushLog.d(TAG, "Build.MANUFACTURER:" + Build.MANUFACTURER);
        return "xiaomi".equalsIgnoreCase(Build.BRAND) || "xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isHuawei(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "Build.MODEL:" + Build.MODEL);
        PushLog.d(TAG, "Build.BRAND:" + Build.BRAND);
        PushLog.d(TAG, "Build.MANUFACTURER:" + Build.MANUFACTURER);
        if (ConstProp.NT_AUTH_NAME_GOOGLE.equalsIgnoreCase(Build.BRAND)) {
            return false;
        }
        if (isHonor(context) && Honor.getInst().checkSupportHonorPush(context)) {
            return false;
        }
        return "huawei".equalsIgnoreCase(Build.BRAND) || "huawei".equalsIgnoreCase(Build.MANUFACTURER) || "HONOR".equalsIgnoreCase(Build.BRAND) || "HONOR".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isFlyme(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "Build.MODEL:" + Build.MODEL);
        PushLog.d(TAG, "Build.BRAND:" + Build.BRAND);
        PushLog.d(TAG, "Build.MANUFACTURER:" + Build.MANUFACTURER);
        if (ConstProp.NT_AUTH_NAME_GOOGLE.equalsIgnoreCase(Build.BRAND)) {
            return false;
        }
        return "meizu".equalsIgnoreCase(Build.BRAND) || "meizu".equalsIgnoreCase(Build.MANUFACTURER) || "flyme".equalsIgnoreCase(Build.BRAND) || "flyme".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isOPPO(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "Build.MODEL:" + Build.MODEL);
        PushLog.d(TAG, "Build.BRAND:" + Build.BRAND);
        PushLog.d(TAG, "Build.MANUFACTURER:" + Build.MANUFACTURER);
        return "oppo".equalsIgnoreCase(Build.BRAND) || "oppo".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isVivo(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "Build.MODEL:" + Build.MODEL);
        PushLog.d(TAG, "Build.BRAND:" + Build.BRAND);
        PushLog.d(TAG, "Build.MANUFACTURER:" + Build.MANUFACTURER);
        return "vivo".equalsIgnoreCase(Build.BRAND) || "vivo".equalsIgnoreCase(Build.MANUFACTURER);
    }

    public static boolean isHonor(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "Build.MODEL:" + Build.MODEL);
        PushLog.d(TAG, "Build.BRAND:" + Build.BRAND);
        PushLog.d(TAG, "Build.MANUFACTURER:" + Build.MANUFACTURER);
        return PushConstantsImpl.HONOR.equalsIgnoreCase(Build.BRAND) || PushConstantsImpl.HONOR.equalsIgnoreCase(Build.MANUFACTURER);
    }
}