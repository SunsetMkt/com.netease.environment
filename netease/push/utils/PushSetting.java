package com.netease.push.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.netease.inner.pushclient.NativePushData;
import com.netease.ntunisdk.base.PatchPlaceholder;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.json.JSONException;

/* loaded from: classes3.dex */
public class PushSetting {
    private static final String FILE_NAME = "neteasepush";
    private static final String KEY_APPID = "appid";
    private static final String KEY_APPKEY = "appkey";
    private static final String KEY_FIRST_START = "firststart";
    private static final String KEY_LIGHT = "light";
    private static final String KEY_NO_REPEAT_INTERVAL = "norepeatinterval";
    private static final String KEY_PUSHNAMES = "pushnames";
    private static final String KEY_RECEIVETIME = "receivetime";
    private static final String KEY_REGISTRATION_ID = "registrationid";
    private static final String KEY_REPEAT_PROTECT = "repeatprotect";
    private static final String KEY_SENDER_ID = "senderid";
    private static final String KEY_SERVICE_TYPE = "servicetype";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_VERCODE = "vercode";
    private static final String KEY_VIBRATE = "vibrate";
    public static final int PERMISSION_REQ_CODE = 0;
    private static final String SEPARATOR = ",";
    private static final String SYSTEM_CUR_PACKAGE = "com.netease.push.curpkg";
    private static final String SYSTEM_CUR_USE_NIEPUSH = "com.netease.push.curuseniepush";
    private static final String SYSTEM_CUR_VERCODE = "com.netease.push.curvercode";
    private static final String SYSTEM_DEV_ID = "com.netease.push.devid2";
    private static final String SYSTEM_HEAD = "com.netease.push.";
    private static final String SYSTEM_NIEPUSH_MODE = "com.netease.push.niepushmode";
    private static final String SYSTEM_PACKAGES = "com.netease.push.packages";
    private static final String SYSTEM_PUSH_ADDR = "com.netease.push.pushaddr";
    private static final String TAG = "NGPush_" + PushSetting.class.getSimpleName();

    public static final void setCurPkg(Context context, String str) {
    }

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    private static boolean putString(Context context, String str, String str2) {
        return FileUtils.write(context, str, str2);
    }

    private static String getString(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "getString name:" + str + ",def:" + str2);
        return FileUtils.read(context, str, str2);
    }

    private static boolean putInt(Context context, String str, int i) {
        return FileUtils.write(context, str, i + "");
    }

    private static int getInt(Context context, String str, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            return Integer.parseInt(FileUtils.read(context, str, i + ""));
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
            return i;
        }
    }

    private static final SharedPreferences getMultiProcessShared(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        } catch (Exception e) {
            PushLog.e(TAG, "getMultiProcessShared:" + e.toString());
            return null;
        }
    }

    public static final String getDevId(Context context) {
        PushLog.i(TAG, "getDevId, context:" + context);
        return getString(context, SYSTEM_DEV_ID.replace("com.netease.push.", context.getPackageName() + "."), getMultiProcessShared(context).getString(SYSTEM_DEV_ID, ""));
    }

    public static final void setDevId(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setDevId, context:" + context + ", devid:" + str);
        getMultiProcessShared(context).edit().putString(SYSTEM_DEV_ID, str).commit();
        StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append(".");
        putString(context, SYSTEM_DEV_ID.replace("com.netease.push.", sb.toString()), str);
    }

    public static final void setKeyVaule(Context context, String str, String str2) {
        PushLog.i(TAG, "setKeyVaule, context:" + context + ", key:" + str + ", value:" + str2);
        getMultiProcessShared(context).edit().putString(str, str2).commit();
        putString(context, str, str2);
    }

    public static final String getVaule(Context context, String str) {
        PushLog.i(TAG, "getVaule, context:" + context + ", key:" + str);
        return getString(context, str, getMultiProcessShared(context).getString(str, ""));
    }

    public static final String getPushAddr(Context context) {
        return getString(context, SYSTEM_PUSH_ADDR, getMultiProcessShared(context).getString(SYSTEM_PUSH_ADDR, ""));
    }

    public static final void setPushAddr(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        getMultiProcessShared(context).edit().putString(SYSTEM_PUSH_ADDR, str).commit();
        if (putString(context, SYSTEM_PUSH_ADDR, str)) {
            return;
        }
        PushLog.e(TAG, "set push addr failed");
    }

    public static final String getCurPkg(Context context) {
        return context.getPackageName();
    }

    public static final int getCurVerCode(Context context) {
        return getInt(context, SYSTEM_CUR_VERCODE, getMultiProcessShared(context).getInt(SYSTEM_CUR_VERCODE, 0));
    }

    public static final void setCurVerCode(Context context, int i) {
        getMultiProcessShared(context).edit().putInt(SYSTEM_CUR_VERCODE, i).commit();
        putInt(context, SYSTEM_CUR_VERCODE, i);
    }

    public static final boolean checkUseNiepush(Context context, boolean z) {
        int niepushMode = getNiepushMode(context, 1);
        if (2 == niepushMode) {
            return true;
        }
        if (niepushMode == 0) {
            return false;
        }
        return z;
    }

    public static final int getNiepushMode(Context context, int i) {
        return getInt(context, SYSTEM_NIEPUSH_MODE, getMultiProcessShared(context).getInt(SYSTEM_NIEPUSH_MODE, i));
    }

    public static final void setNiepushMode(Context context, int i) {
        getMultiProcessShared(context).edit().putInt(SYSTEM_NIEPUSH_MODE, i).commit();
        putInt(context, SYSTEM_NIEPUSH_MODE, i);
    }

    public static final boolean getCurUseNiepush(Context context, boolean z) {
        return checkUseNiepush2(context, true);
    }

    public static final void setCurUseNiepush(Context context, boolean z) {
        getMultiProcessShared(context).edit().putInt(SYSTEM_CUR_USE_NIEPUSH, z ? 1 : 0).commit();
        putInt(context, SYSTEM_CUR_USE_NIEPUSH, z ? 1 : 0);
    }

    public static final Set<String> getPackages(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String string = getString(context, SYSTEM_PACKAGES, getMultiProcessShared(context).getString(SYSTEM_PACKAGES, ""));
        PushLog.d(TAG, context.getPackageName() + " getPackages:" + string);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return new HashSet(Arrays.asList(TextUtils.split(string, ",")));
    }

    public static final void setPackages(Context context, Set<String> set) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String strJoin = TextUtils.join(",", (String[]) set.toArray(new String[set.size()]));
        getMultiProcessShared(context).edit().putString(SYSTEM_PACKAGES, strJoin).commit();
        putString(context, SYSTEM_PACKAGES, strJoin);
        PushLog.d(TAG, context.getPackageName() + " setPackages:" + strJoin);
    }

    private static final SharedPreferences getFileShared(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences defaultSharedPreferences;
        try {
            if (context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                defaultSharedPreferences = new MySharedPreferences(context, str);
            } else {
                defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            }
            return defaultSharedPreferences;
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
            return null;
        }
    }

    public static final boolean checkUseNiepush2(Context context, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int niepushMode2 = getNiepushMode2(context, 1);
        if (2 == niepushMode2) {
            return true;
        }
        if (niepushMode2 == 0) {
            return false;
        }
        return z;
    }

    public static final int getNiepushMode2(Context context, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        return fileShared == null ? i : fileShared.getInt(SYSTEM_NIEPUSH_MODE, i);
    }

    public static final void setNiepushMode2(Context context, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putInt(SYSTEM_NIEPUSH_MODE, i).commit();
    }

    public static final String getServiceType(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, str);
        return fileShared == null ? "niepush" : fileShared.getString(KEY_SERVICE_TYPE, "niepush");
    }

    public static final void setServiceType(Context context, String str) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putString(KEY_SERVICE_TYPE, str).commit();
    }

    public static final int getVerCode(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, str);
        if (fileShared == null) {
            return 0;
        }
        return fileShared.getInt(KEY_VERCODE, 0);
    }

    public static final void setVerCode(Context context, int i) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putInt(KEY_VERCODE, i).commit();
    }

    public static final void setFirstStart(Context context, String str, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, str);
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putBoolean(KEY_FIRST_START, z).commit();
    }

    public static final void setSound(Context context, boolean z) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putBoolean("sound", z).commit();
    }

    public static final void setVibrate(Context context, boolean z) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putBoolean(KEY_VIBRATE, z).commit();
    }

    public static final void setLight(Context context, boolean z) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putBoolean(KEY_LIGHT, z).commit();
    }

    public static final void enableRepeatProtect(Context context, boolean z) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putBoolean(KEY_REPEAT_PROTECT, z).commit();
    }

    public static final void setRepeatProtectInterval(Context context, int i) {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putInt("norepeatinterval", i).commit();
    }

    public static final long getReceiveTime(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return 0L;
        }
        return fileShared.getLong(KEY_RECEIVETIME, 0L);
    }

    public static final void setReceiveTime(Context context, long j) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putLong(KEY_RECEIVETIME, j).commit();
    }

    public static final AppInfo getAppInfo(Context context) {
        if (context != null) {
            return getAppInfo(context, context.getPackageName());
        }
        return null;
    }

    public static final AppInfo getAppInfo(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        AppInfo appInfo = new AppInfo(str);
        SharedPreferences fileShared = getFileShared(context, str);
        if (fileShared == null) {
            return appInfo;
        }
        appInfo.mbEnableSound = fileShared.getBoolean("sound", false);
        appInfo.mbEnableVibrate = fileShared.getBoolean(KEY_VIBRATE, true);
        appInfo.mbEnableLight = fileShared.getBoolean(KEY_LIGHT, false);
        appInfo.mLastReceiveTime = fileShared.getLong(KEY_RECEIVETIME, 0L);
        appInfo.mbRepeatProtect = fileShared.getBoolean(KEY_REPEAT_PROTECT, false);
        appInfo.mNoRepeatInterval = fileShared.getInt("norepeatinterval", 300);
        appInfo.mbFirstStart = fileShared.getBoolean(KEY_FIRST_START, true);
        return appInfo;
    }

    public static final Set<String> getNativePushNames(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashSet hashSet = new HashSet();
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return hashSet;
        }
        String string = fileShared.getString(KEY_PUSHNAMES, "");
        return TextUtils.isEmpty(string) ? hashSet : new TreeSet(Arrays.asList(TextUtils.split(string, ",")));
    }

    public static final void setNativePushNames(Context context, Set<String> set) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "setNativePushNames, pushSet:" + set);
        String strJoin = TextUtils.join(",", (String[]) set.toArray(new String[set.size()]));
        SharedPreferences fileShared = getFileShared(context, context.getPackageName());
        if (fileShared == null) {
            return;
        }
        fileShared.edit().putString(KEY_PUSHNAMES, strJoin).commit();
    }

    public static final void rmNativePushName(Context context, String str) {
        PushLog.d(TAG, "rmNativePushNames, pushName:" + str);
        Set<String> nativePushNames = getNativePushNames(context);
        if (nativePushNames.contains(str)) {
            nativePushNames.remove(str);
            delNativeNotification(context, str);
            setNativePushNames(context, nativePushNames);
        }
    }

    public static final void rmAllNativePushNames(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "rmAllNativePushNames");
        Set<String> nativePushNames = getNativePushNames(context);
        Iterator<String> it = nativePushNames.iterator();
        while (it.hasNext()) {
            delNativeNotification(context, it.next());
        }
        nativePushNames.clear();
        setNativePushNames(context, nativePushNames);
    }

    public static final boolean setNativeNotification(Context context, NativePushData nativePushData) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setNativeNotification, pushName:" + nativePushData.getPushName());
        try {
            SharedPreferences fileShared = getFileShared(context, context.getPackageName());
            if (fileShared == null) {
                return false;
            }
            fileShared.edit().putString(nativePushData.getPushName(), nativePushData.writeToJsonString()).commit();
            return true;
        } catch (Exception e) {
            PushLog.d(TAG, "setNativeNotification exception:" + e.toString());
            return false;
        }
    }

    public static final void delNativeNotification(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "delNativeNotification, pushName:" + str);
        try {
            SharedPreferences fileShared = getFileShared(context, context.getPackageName());
            if (fileShared == null) {
                return;
            }
            fileShared.edit().remove(str).commit();
        } catch (Exception e) {
            PushLog.d(TAG, "delNativeNotification exception:" + e.toString());
        }
    }

    public static final NativePushData getNativeNotification(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            SharedPreferences fileShared = getFileShared(context, context.getPackageName());
            if (fileShared == null) {
                return null;
            }
            String string = fileShared.getString(str, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            return NativePushData.readFromJsonString(str, string);
        } catch (Exception e) {
            PushLog.d(TAG, "getNativeNotification exception:" + e.toString());
            return null;
        }
    }

    public static final List<NativePushData> getAllOtherNativeNotifications(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ArrayList arrayList;
        SharedPreferences fileShared;
        NativePushData fromJsonString;
        try {
            fileShared = getFileShared(context, context.getPackageName());
        } catch (Exception e) {
            e = e;
            arrayList = null;
        }
        if (fileShared == null) {
            return null;
        }
        arrayList = new ArrayList();
        try {
            Set<String> nativePushNames = getNativePushNames(context);
            PushLog.d(TAG, "getNativePushNames, pushSet:" + nativePushNames);
            for (String str2 : nativePushNames) {
                String string = fileShared.getString(str2, "");
                if (!TextUtils.isEmpty(string)) {
                    try {
                        fromJsonString = NativePushData.readFromJsonString(str2, string);
                    } catch (JSONException e2) {
                        PushLog.d(TAG, "NativePushData.readFromJsonString exception:" + e2.toString());
                        fromJsonString = null;
                    }
                    if (fromJsonString != null) {
                        arrayList.add(fromJsonString);
                    }
                }
            }
        } catch (Exception e3) {
            e = e3;
            PushLog.d(TAG, "getAllOtherNativeNotifications exception:" + e.toString());
            return arrayList;
        }
        return arrayList;
    }

    private static final SharedPreferences getCurShared(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static final String getSenderID(Context context, String str) {
        return getCurShared(context).getString(str + "." + KEY_SENDER_ID, "");
    }

    public static final void setSenderID(Context context, String str, String str2) {
        getCurShared(context).edit().putString(str + "." + KEY_SENDER_ID, str2).commit();
    }

    public static final String getAppID(Context context, String str) {
        return getCurShared(context).getString(str + ".appid", "");
    }

    public static final void setAppID(Context context, String str, String str2) {
        getCurShared(context).edit().putString(str + ".appid", str2).commit();
    }

    public static final String getAppKey(Context context, String str) {
        return getCurShared(context).getString(str + ".appkey", "");
    }

    public static final void setAppKey(Context context, String str, String str2) {
        getCurShared(context).edit().putString(str + ".appkey", str2).commit();
    }

    public static final String getRegistrationID(Context context, String str) {
        return getCurShared(context).getString(str + "." + KEY_REGISTRATION_ID, "");
    }

    public static final void setRegistrationID(Context context, String str, String str2) {
        getCurShared(context).edit().putString(str + "." + KEY_REGISTRATION_ID, str2).commit();
    }
}