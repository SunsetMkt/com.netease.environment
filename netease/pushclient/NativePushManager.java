package com.netease.pushclient;

import android.content.Context;
import android.util.Log;
import com.netease.push.utils.PushConstants;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes3.dex */
public class NativePushManager {
    private static final String TAG = "NGPush_NativePushManager";
    private static Class<?> s_clazzImpl;

    public static void init(Context context) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "init, context:" + context);
        try {
            Class<?> cls = Class.forName("com.netease.pushclient.NativePushManagerImpl");
            s_clazzImpl = cls;
            cls.getMethod("init", Context.class).invoke(null, context);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "init exception:" + e.getMessage());
        }
    }

    public static boolean newAlarm(String str, String str2, String str3, String str4) {
        return newAlarm(str, str2, str3, str4, "");
    }

    public static boolean newAlarm(String str, String str2, String str3, String str4, String str5) {
        return newAlarm(str, str2, str3, str4, str5, "", "", "", "");
    }

    public static boolean newAlarm(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("newAlarm", String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class).invoke(null, str, str2, str3, str4, str5, str6, str7, str8, str9)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "newAlarm exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setAlarmTime(String str, int i, int i2) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setAlarmTime", String.class, Integer.TYPE, Integer.TYPE).invoke(null, str, Integer.valueOf(i), Integer.valueOf(i2))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setAlarmTime exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setAlarmTime(String str, int i, int i2, String str2) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setAlarmTime", String.class, Integer.TYPE, Integer.TYPE, String.class).invoke(null, str, Integer.valueOf(i), Integer.valueOf(i2), str2)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setAlarmTime exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setAlarmTime(String str, int i, int i2, int i3, String str2) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setAlarmTime", String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, String.class).invoke(null, str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), str2)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setAlarmTime exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setWeekRepeat(String str, int i) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setWeekRepeat", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(i))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setWeekRepeat exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setWeekRepeatNew(String str, int i) {
        int i2;
        switch (i) {
            case 1:
                i2 = 1;
                break;
            case 2:
                i2 = 2;
                break;
            case 3:
                i2 = 4;
                break;
            case 4:
                i2 = 8;
                break;
            case 5:
                i2 = 16;
                break;
            case 6:
                i2 = 32;
                break;
            case 7:
                i2 = 64;
                break;
            default:
                i2 = 0;
                break;
        }
        try {
            return ((Boolean) s_clazzImpl.getMethod("setWeekRepeat", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(i2))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setWeekRepeat exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setMonthRepeat(String str, int i) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setMonthRepeat", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(i))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setMonthRepeat exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setMonthRepeatNew(String str, int i) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setMonthRepeat", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(PushConstants.MONTH_DAY(i)))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setMonthRepeat exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setMonthRepeatBackwards(String str, int i) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setMonthRepeatBackwards", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(i))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setMonthRepeatBackwards exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setOnce(String str, int i, int i2, int i3) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setOnce", String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setOnce exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setOnceNew(String str, int i, int i2, int i3) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setOnce", String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, str, Integer.valueOf(i), Integer.valueOf(i2 - 1), Integer.valueOf(i3))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setOnce exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setOnceUnixtime(String str, long j) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setOnceUnixtime", String.class, Long.TYPE).invoke(null, str, Long.valueOf(j))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setOnceUnixtime exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean setOnceLater(String str, int i) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("setOnceLater", String.class, Integer.TYPE).invoke(null, str, Integer.valueOf(i))).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setOnceLater exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean startAlarm(String str) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("startAlarm", String.class).invoke(null, str)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "startAlarm exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean stopPush(String str) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("stopPush", String.class).invoke(null, str)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "stopPush exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean removeAlarm(String str) {
        try {
            return ((Boolean) s_clazzImpl.getMethod("removeAlarm", String.class).invoke(null, str)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "removeAlarm exception:" + e.getMessage());
            return false;
        }
    }

    public static boolean removeAllAlarms() {
        try {
            return ((Boolean) s_clazzImpl.getMethod("removeAllAlarms", new Class[0]).invoke(null, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "removeAlarm exception:" + e.getMessage());
            return false;
        }
    }

    public static String[] getAllAlarms() {
        try {
            return (String[]) s_clazzImpl.getMethod("getAllAlarms", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getAllAlarms exception:" + e.getMessage());
            return null;
        }
    }

    public static void clearContext() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "clearContext");
        try {
            s_clazzImpl.getMethod("clearContext", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "clearContext exception:" + e.getMessage());
        }
    }
}