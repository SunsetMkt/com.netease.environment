package com.netease.pushclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.push.utils.PushLog;
import com.netease.pushservice.IdCache;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class UnisdkDeviceUtil {
    private static SharedPreferences sharedPreferences;
    private static final String TAG = "NGPush_" + UnisdkDeviceUtil.class.getSimpleName();
    private static HashSet<Callback> sCallbackSet = new HashSet<>();
    public static String unisdkAreaZone = "";
    public static String unisdkTimeZone = "";
    public static String unisdkmacAddress = "";
    public static String unisdkimei = "";

    public interface Callback {
        void done(String str);
    }

    public interface GaidCallback extends Callback {
    }

    public static String getUnisdkDeviceId(Context context, GaidCallback gaidCallback) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String androidId = "";
        if (context == null) {
            PushLog.d(TAG, "context is null");
            return "unknown_activity_Notcreate_or_Notset";
        }
        try {
            PushLog.d(TAG, "getAndroidId");
            androidId = getAndroidId(context);
            UniSdkUtils.d(TAG, "getUnisdkDeviceId -> deviceId: " + androidId);
            return androidId;
        } catch (Throwable th) {
            th.printStackTrace();
            return androidId;
        }
    }

    public static String getAndroidId(Context context) {
        return context != null ? getUnisdkAndroidId(context) : "";
    }

    public static String getUnisdkAndroidId(Context context) throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "getUnisdkAndroidId");
        if (!TextUtils.isEmpty(IdCache.androidId)) {
            PushLog.d(TAG, "getUnisdkAndroidId -> use IdCache.androidId, androidId= " + IdCache.androidId);
            return IdCache.androidId;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getUnisdkDeviceId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ModulesManager.getInst().extendFunc("ngpush", "deviceInfo", jSONObject.toString());
    }

    public static boolean isDomestic(Context context) {
        return hasInstalledGooglePlayServices(context);
    }

    public static boolean hasInstalledGooglePlayServices(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.google.android.gms", 0);
            PushLog.i(TAG, "gms pkgInfo: " + packageInfo);
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo("com.google.android.gms", 0);
            PushLog.i(TAG, "gms appInfo: " + applicationInfo);
            PackageInfo packageInfo2 = packageManager.getPackageInfo("com.android.vending", 0);
            PushLog.i(TAG, "store pkgInfo: " + packageInfo2);
            ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo("com.android.vending", 0);
            PushLog.i(TAG, "store appInfo: " + applicationInfo2);
            return true;
        } catch (Throwable unused) {
            PushLog.w(TAG, "Google Play services is missing.");
            return false;
        }
    }

    private static boolean isInvalidImei(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        char cCharAt = str.charAt(0);
        for (int i = 0; i != str.length(); i++) {
            if (str.charAt(i) != cCharAt) {
                return false;
            }
        }
        return true;
    }

    public static String getMobileIMEI(Context context) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getImei");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ModulesManager.getInst().extendFunc("ngpush", "deviceInfo", jSONObject.toString());
    }

    public static String getMacAddress(Context context) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getMacAddress");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ModulesManager.getInst().extendFunc("ngpush", "deviceInfo", jSONObject.toString());
    }

    public static String getTimeZone() throws JSONException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getTimeZone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc("ngpush", "deviceInfo", jSONObject.toString());
        PushLog.i(TAG, "getTimeZone: " + strExtendFunc);
        return strExtendFunc;
    }

    public static String getAreaZone() throws JSONException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getAreaZone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String strExtendFunc = ModulesManager.getInst().extendFunc("ngpush", "deviceInfo", jSONObject.toString());
        PushLog.i(TAG, "getAreaZone: " + strExtendFunc);
        return strExtendFunc;
    }

    public static String getTransId() throws IllegalAccessException, JSONException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "getTransId");
        if (!TextUtils.isEmpty(IdCache.transId)) {
            PushLog.d(TAG, "getTransId -> use IdCache.getTransId, transId= " + IdCache.transId);
            return IdCache.transId;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getTransId");
            return ModulesManager.getInst().extendFunc("ngpush", "deviceInfo", jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMobileModel() {
        return Build.MODEL;
    }
}