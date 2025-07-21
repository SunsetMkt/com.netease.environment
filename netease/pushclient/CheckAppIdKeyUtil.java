package com.netease.pushclient;

import android.content.Context;
import android.content.pm.PackageManager;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class CheckAppIdKeyUtil {
    private static final String EFFECTIVE_FLYME_APP_ID = "effective_flyme_app_id";
    private static final String EFFECTIVE_FLYME_APP_KEY = "effective_flyme_app_key";
    private static final String EFFECTIVE_HMS_APP_ID = "effective_hms_app_id";
    private static final String EFFECTIVE_HONOR_APP_ID = "effective_honor_app_id";
    private static final String EFFECTIVE_HUAWEI_APP_ID = "effective_huawei_app_id";
    private static final String EFFECTIVE_MIUI_APP_ID = "effective_miui_app_id";
    private static final String EFFECTIVE_MIUI_APP_KEY = "effective_miui_app_key";
    private static final String EFFECTIVE_OPPO_APP_ID = "effective_oppo_app_id";
    private static final String EFFECTIVE_OPPO_APP_KEY = "effective_oppo_app_key";
    private static final String EFFECTIVE_VIVO_APP_ID = "effective_vivo_app_id";
    private static final String EFFECTIVE_VIVO_APP_KEY = "effective_vivo_app_key";
    private static final String TAG = "NGPush_" + CheckAppIdKeyUtil.class.getSimpleName();
    private static final HashMap<String, String> appIdRecordMap = new HashMap<>();
    private static final HashMap<String, String> appKeyRecordMap = new HashMap<>();

    public static void recordSetAppId(String str, String str2) {
        appIdRecordMap.put(str, str2);
    }

    public static void recordSetAppKey(String str, String str2) {
        appKeyRecordMap.put(str, str2);
    }

    public static void setCustomAppIdKey(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str = appIdRecordMap.get("miui");
        if (str != null) {
            setAppID(context, "miui", str);
            PushLog.d(TAG, "setCustomAppIdKey -> miuiAppId: " + str);
        }
        String str2 = appKeyRecordMap.get("miui");
        if (str2 != null) {
            setAppKey(context, "miui", str2);
            PushLog.d(TAG, "setCustomAppIdKey -> miuiAppKey: " + str2);
        }
        String str3 = appIdRecordMap.get("huawei");
        if (str3 != null) {
            setAppID(context, "huawei", str3);
            setAppID(context, "hms", str3);
            PushLog.d(TAG, "setCustomAppIdKey -> huaweiAppId: " + str3);
        }
        String str4 = appIdRecordMap.get("hms");
        if (str4 != null) {
            setAppID(context, "huawei", str4);
            setAppID(context, "hms", str4);
            PushLog.d(TAG, "setCustomAppIdKey -> hmsAppId: " + str4);
        }
        String str5 = appIdRecordMap.get("flyme");
        if (str5 != null) {
            setAppID(context, "flyme", str5);
            PushLog.d(TAG, "setCustomAppIdKey -> flymeAppId: " + str5);
        }
        String str6 = appKeyRecordMap.get("flyme");
        if (str6 != null) {
            setAppKey(context, "flyme", str6);
            PushLog.d(TAG, "setCustomAppIdKey -> flymeAppKey: " + str6);
        }
        String str7 = appIdRecordMap.get("oppo");
        if (str7 != null) {
            setAppID(context, "oppo", str7);
            PushLog.d(TAG, "setCustomAppIdKey -> oppoAppId: " + str7);
        }
        String str8 = appKeyRecordMap.get("oppo");
        if (str8 != null) {
            setAppKey(context, "oppo", str8);
            PushLog.d(TAG, "setCustomAppIdKey -> oppoAppKey: " + str8);
        }
        String str9 = appIdRecordMap.get(PushConstantsImpl.HONOR);
        if (str9 != null) {
            setAppID(context, PushConstantsImpl.HONOR, str9);
            PushLog.d(TAG, "setCustomAppIdKey -> honorAppId: " + str9);
        }
        String str10 = appIdRecordMap.get("vivo");
        if (str10 != null) {
            setAppID(context, "vivo", str10);
            PushLog.d(TAG, "setCustomAppIdKey -> vivoAppId: " + str10);
        }
        String str11 = appKeyRecordMap.get("vivo");
        if (str11 != null) {
            setAppKey(context, "vivo", str11);
            PushLog.d(TAG, "setCustomAppIdKey -> vivoAppKey: " + str11);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean checkAppIdKeyChanged(android.content.Context r10, java.lang.String r11, java.lang.String r12, java.lang.String r13) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 396
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushclient.CheckAppIdKeyUtil.checkAppIdKeyChanged(android.content.Context, java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void saveEffectiveAppIdKey(android.content.Context r5, java.lang.String r6) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 286
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushclient.CheckAppIdKeyUtil.saveEffectiveAppIdKey(android.content.Context, java.lang.String):void");
    }

    public static void setAppID(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.netease.inner.pushclient.PushManager.getInstance().setAppID(context, str, str2);
    }

    public static void setAppKey(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.netease.inner.pushclient.PushManager.getInstance().setAppKey(context, str, str2);
    }

    public static void modifyMetaData(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        doModifyMetaData(context, str, str2);
        doModifyMetaData(context.getApplicationContext(), str, str2);
    }

    public static void doModifyMetaData(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            PushLog.d(TAG, "modifyMetaData -> context: " + context);
            PushLog.d(TAG, "modifyMetaData -> key: " + str);
            PushLog.d(TAG, "modifyMetaData -> value: " + str2);
            context.getPackageManager().getPackageInfo(context.getPackageName(), 128).applicationInfo.metaData.putString(str, str2);
            PushLog.d(TAG, "modifyMetaData -> 1 success");
        } catch (PackageManager.NameNotFoundException e) {
            PushLog.e(TAG, "modifyMetaData -> e: " + e);
        }
        try {
            context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.putString(str, str2);
            PushLog.d(TAG, "modifyMetaData -> 2 success");
        } catch (PackageManager.NameNotFoundException e2) {
            PushLog.e(TAG, "modifyMetaData -> e: " + e2);
        }
    }
}