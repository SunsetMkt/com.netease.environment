package com.netease.pushclient;

import android.content.Context;
import com.netease.inner.pushclient.NativePushData;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes3.dex */
public class NativePushManagerImpl {
    private static final String TAG = "NGPush_" + NativePushManagerImpl.class.getSimpleName();
    public static Context mContext = null;

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        mContext = context;
        if (mContext != null) {
            com.netease.inner.pushclient.NativePushManager.getInstance().init(mContext);
        }
    }

    public static boolean newAlarm(String str, String str2, String str3, String str4) {
        return newAlarm(str, str2, str3, str4, "");
    }

    public static boolean newAlarm(String str, String str2, String str3, String str4, String str5) {
        return newAlarm(str, str2, str3, str4, str5, "", "", "", "");
    }

    public static boolean newAlarm(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().newAlarm(str, str2, str3, str4, str5, str6, str7, str8, str9);
        }
        return false;
    }

    public static boolean setAlarmTime(String str, int i, int i2) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setAlarmTime(str, i, i2);
        }
        return false;
    }

    public static boolean setAlarmTime(String str, int i, int i2, String str2) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setAlarmTime(str, i, i2, str2);
        }
        return false;
    }

    public static boolean setAlarmTime(String str, int i, int i2, int i3, String str2) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setAlarmTime(str, i, i2, i3, str2);
        }
        return false;
    }

    public static boolean setWeekRepeat(String str, int i) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setWeekRepeat(str, i);
        }
        return false;
    }

    public static boolean setMonthRepeat(String str, int i) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setMonthRepeat(str, i);
        }
        return false;
    }

    public static boolean setMonthRepeatBackwards(String str, int i) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setMonthRepeatBackwards(str, i);
        }
        return false;
    }

    public static boolean setOnce(String str, int i, int i2, int i3) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setOnce(str, i, i2, i3);
        }
        return false;
    }

    public static boolean setOnceUnixtime(String str, long j) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setOnceUnixtime(str, j);
        }
        return false;
    }

    public static boolean setOnceLater(String str, int i) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().setOnceUnixtime(str, (System.currentTimeMillis() / 1000) + i);
        }
        return false;
    }

    public static boolean startAlarm(String str) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().startAlarm(str);
        }
        return false;
    }

    public static boolean startAlarm(NativePushData nativePushData) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().startAlarm(nativePushData);
        }
        return false;
    }

    public static boolean stopPush(String str) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().stopPush(str);
        }
        return false;
    }

    public static boolean removeAlarm(String str) {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().removeAlarm(str);
        }
        return false;
    }

    public static boolean removeAllAlarms() {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().removeAllAlarms();
        }
        return false;
    }

    public static String[] getAllAlarms() {
        if (mContext != null) {
            return com.netease.inner.pushclient.NativePushManager.getInstance().getAllAlarms();
        }
        return null;
    }

    public static void clearContext() {
        if (mContext != null) {
            mContext = null;
        }
    }
}