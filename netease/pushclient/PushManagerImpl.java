package com.netease.pushclient;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.alipay.sdk.m.t.a;
import com.netease.inner.pushclient.gcm.GCM;
import com.netease.inner.pushclient.honor.Honor;
import com.netease.inner.pushclient.huawei.Huawei;
import com.netease.inner.pushclient.oppo.OPPO;
import com.netease.inner.pushclient.vivo.Vivo;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.pharos.Const;
import com.netease.push.utils.DeviceInfo;
import com.netease.push.utils.NotifyMessageImpl;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import com.netease.pushservice.PushServiceHelper;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.api.b;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public final class PushManagerImpl {
    private static final String NOTIFICATIONS_PROMPT_COUNT = "notificationsPromptCount";
    private static final String NOTIFICATIONS_PROMPT_TIMES = "notificationsPromptTimes";
    private static String aid;
    public static volatile Boolean isCloudEnv;
    public static volatile boolean nieGetNewTokenSuccess;
    public static volatile boolean nieLoginSuccess;
    public static String project;
    private static String roleid;
    private static String sdkuid;
    private static String service;
    private static final String TAG = "NGPush_" + PushManagerImpl.class.getSimpleName();
    public static Context s_context = null;
    private static boolean s_multiPackSupport = true;
    private static List<Pair<String, Boolean>> s_permissions = new ArrayList();
    private static boolean s_initialized = false;
    private static int PERMISSION_REQ_CODE = 999;
    public static String subscriber = "";
    private static String gate = "";
    private static String gateKey = "";
    public static String product_id = "";
    public static String client_key = "";
    public static String version = "";
    public static String EB = "";
    private static String EBUploadUrl = "https://sigma-statistics-push.proxima.nie.easebar.com";
    public static String EBgate = "https://sdkgate.pushv3.easebar.com:25004";
    public static String sdkgate = "https://sdkgate.pushv3.netease.com:25004";
    private static String token = "";
    private static String access_key = "";
    public static String type = "";
    public static String findTokenResult = "";
    public static boolean hasSetProductId = false;
    public static String permissionTips = "";
    public static String goToPermissionTips = "";
    public static String refuseTips = "";
    public static boolean showPermissionDialog = true;
    private static Map<String, Boolean> usePlatform = new HashMap<String, Boolean>() { // from class: com.netease.pushclient.PushManagerImpl.1
        {
            put("miui", true);
            put("huawei", true);
            put("hms", true);
            put("flyme", true);
            put("fcm", true);
            put("oppo", true);
            put("vivo", true);
            put(PushConstantsImpl.HONOR, true);
        }
    };

    public interface HmsCallback {
        void hmsFail();

        void hmsSuccess();
    }

    public interface PushManagerCallback {
        void onInitFailed(String str);

        void onInitSuccess();
    }

    public static String getSdkVersion() {
        return PushConstantsImpl.SDK_VERSION;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void onInitSuccessFinal() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PushU3d.CALLBACKTYPE_onInitSuccess);
        try {
            Class.forName("com.netease.pushclient.PushManager").getMethod(PushU3d.CALLBACKTYPE_onInitSuccess, new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
    }

    private static void onInitSuccess() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Context context = s_context;
        if (context != null) {
            String serviceType = getServiceType(context);
            if ("hms".equals(serviceType)) {
                PushLog.d(TAG, "checkHms");
                Huawei.getInst().checkHms(s_context, new HmsCallback() { // from class: com.netease.pushclient.PushManagerImpl.2
                    @Override // com.netease.pushclient.PushManagerImpl.HmsCallback
                    public void hmsSuccess() throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
                        PushLog.d(PushManagerImpl.TAG, "hmsSuccess");
                        int i = 0;
                        try {
                            Class<?> cls = Class.forName("android.os.SystemProperties");
                            i = Integer.parseInt((String) cls.getDeclaredMethod(h.c, String.class).invoke(cls, "ro.build.hw_emui_api_level"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PushLog.d(PushManagerImpl.TAG, "EMUI\u7248\u672c:" + i);
                        if (i < 10) {
                            PushLog.d(PushManagerImpl.TAG, "EMUI\u7248\u672c\u4f4e\u4e8e4.1\u4f7f\u7528niepush!");
                            PushManagerImpl.setServiceType(PushManagerImpl.s_context, "niepush");
                        }
                        PushManagerImpl.onInitSuccessFinal();
                    }

                    @Override // com.netease.pushclient.PushManagerImpl.HmsCallback
                    public void hmsFail() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                        PushLog.d(PushManagerImpl.TAG, "hmsFail");
                        PushManagerImpl.setServiceType(PushManagerImpl.s_context, "niepush");
                        PushManagerImpl.onInitSuccessFinal();
                    }
                });
                return;
            }
            if ("oppo".equals(serviceType)) {
                PushLog.d(TAG, "checkSupportOPPOPush");
                if (OPPO.getInst().checkSupportOPPOPush(s_context)) {
                    PushLog.d(TAG, "checkSupportOPPOPush true");
                    onInitSuccessFinal();
                    return;
                } else {
                    PushLog.d(TAG, "checkSupportOPPOPush false");
                    setServiceType(s_context, "niepush");
                    onInitSuccessFinal();
                    return;
                }
            }
            if ("vivo".equals(serviceType)) {
                PushLog.d(TAG, "checkSupportVIVOPush");
                if (Vivo.getInst().checkSupportVIVOPush(s_context)) {
                    PushLog.d(TAG, "checkSupportVIVOPush true");
                    onInitSuccessFinal();
                    return;
                } else {
                    PushLog.d(TAG, "checkSupportVIVOPush false");
                    setServiceType(s_context, "niepush");
                    onInitSuccessFinal();
                    return;
                }
            }
            if (PushConstantsImpl.HONOR.equals(serviceType)) {
                PushLog.d(TAG, "checkSupportHonorPush");
                if (Honor.getInst().checkSupportHonorPush(s_context)) {
                    PushLog.d(TAG, "checkSupportHonorPush true");
                    onInitSuccessFinal();
                    return;
                } else {
                    PushLog.d(TAG, "checkSupportHonorPush false");
                    setServiceType(s_context, "niepush");
                    onInitSuccessFinal();
                    return;
                }
            }
            onInitSuccessFinal();
            return;
        }
        onInitSuccessFinal();
    }

    private static void onInitFailed(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "onInitFailed, reason:" + str);
        try {
            Class.forName("com.netease.pushclient.PushManager").getMethod(PushU3d.CALLBACKTYPE_onInitFailed, Integer.TYPE, String.class).invoke(null, 2, str);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
        try {
            Class.forName("com.netease.pushclient.PushManager").getMethod("onCallbackFailed", String.class, Integer.TYPE, String.class).invoke(null, new JSONObject().toString(), Integer.valueOf(Const.UDP_PORT), str);
        } catch (Exception e2) {
            PushLog.e(TAG, e2.toString());
        }
    }

    public static void killProcess(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str;
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        int iMyPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
        while (true) {
            if (!it.hasNext()) {
                str = "";
                break;
            }
            ActivityManager.RunningAppProcessInfo next = it.next();
            if (iMyPid == next.pid) {
                str = next.processName;
                break;
            }
        }
        PushLog.i(TAG, "mainProcessName = " + str);
        if (TextUtils.isEmpty(str)) {
            Process.killProcess(iMyPid);
            return;
        }
        ArrayList<Integer> arrayList = new ArrayList();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.processName.contains(str) && !runningAppProcessInfo.processName.equals(str)) {
                arrayList.add(Integer.valueOf(runningAppProcessInfo.pid));
            }
        }
        for (Integer num : arrayList) {
            PushLog.i(TAG, "id = " + num);
            Process.killProcess(num.intValue());
        }
        Process.killProcess(iMyPid);
        System.exit(0);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x0549 A[Catch: Exception -> 0x05b0, TRY_LEAVE, TryCatch #0 {Exception -> 0x05b0, blocks: (B:99:0x053d, B:101:0x0549, B:105:0x05ac, B:102:0x058b), top: B:111:0x053d, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x034b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x03eb A[Catch: Exception -> 0x0514, TryCatch #6 {Exception -> 0x0514, blocks: (B:72:0x033f, B:81:0x03e3, B:83:0x03eb, B:84:0x03f4, B:86:0x03fc, B:87:0x0405, B:89:0x048b, B:90:0x0490, B:94:0x04dd, B:93:0x04da, B:80:0x03b9, B:75:0x034b, B:77:0x0396), top: B:123:0x033f, inners: #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x03fc A[Catch: Exception -> 0x0514, TryCatch #6 {Exception -> 0x0514, blocks: (B:72:0x033f, B:81:0x03e3, B:83:0x03eb, B:84:0x03f4, B:86:0x03fc, B:87:0x0405, B:89:0x048b, B:90:0x0490, B:94:0x04dd, B:93:0x04da, B:80:0x03b9, B:75:0x034b, B:77:0x0396), top: B:123:0x033f, inners: #3, #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x048b A[Catch: Exception -> 0x0514, TRY_LEAVE, TryCatch #6 {Exception -> 0x0514, blocks: (B:72:0x033f, B:81:0x03e3, B:83:0x03eb, B:84:0x03f4, B:86:0x03fc, B:87:0x0405, B:89:0x048b, B:90:0x0490, B:94:0x04dd, B:93:0x04da, B:80:0x03b9, B:75:0x034b, B:77:0x0396), top: B:123:0x033f, inners: #3, #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void init(android.content.Context r18) throws java.lang.IllegalAccessException, org.json.JSONException, android.content.res.Resources.NotFoundException, android.content.pm.PackageManager.NameNotFoundException, java.io.IOException, java.lang.ClassNotFoundException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 1461
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushclient.PushManagerImpl.init(android.content.Context):void");
    }

    public static void initWithProductId(Context context, String str, String str2) throws IllegalAccessException, JSONException, Resources.NotFoundException, PackageManager.NameNotFoundException, IOException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        Log.d(TAG, "initWithProductId");
        setProductId(context, str, str2);
        init(context);
    }

    private static void initImpl() throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "initImpl");
        com.netease.inner.pushclient.PushManager.getInstance().init(s_context);
        try {
            Class.forName("com.netease.pushclient.NativePushManager").getMethod("init", Context.class).invoke(null, s_context);
        } catch (Exception e) {
            PushLog.e(TAG, e.toString());
        }
        s_initialized = true;
        checkPushServiceType(s_context);
    }

    public static Context getContext() {
        return s_context;
    }

    public static void enableMultiPackSupport(boolean z) {
        s_multiPackSupport = z;
    }

    public static void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        ModulesManager.getInst().onRequestPermissionsResult(i, strArr, iArr);
    }

    public static void startService() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "startService");
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().startService(s_context);
        }
    }

    public static void stopService() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "stopService");
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().stopService(s_context);
        }
    }

    public static void terminatePush() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        stopService();
        PushServiceHelper.getInstance().stop();
    }

    public static String getDevId(Context context) {
        String registrationID = "";
        if (context == null) {
            return "";
        }
        String serviceType = getServiceType(context);
        PushLog.e(TAG, "getDevId, ctx:" + context + ", s_initialized:" + s_initialized + ", serviceType:" + serviceType);
        String str = context.getApplicationInfo().packageName;
        if ("gcm".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",gcm," + str;
            }
            PushLog.e(TAG, "gcm devid:" + registrationID);
        } else if ("miui".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",miui," + str;
            }
            PushLog.e(TAG, "miui devid:" + registrationID);
        } else if ("huawei".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",huawei," + str;
            }
            PushLog.e(TAG, "huawei devid:" + registrationID);
        } else if ("hms".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",hms," + str;
            }
            PushLog.e(TAG, "huawei hms devid:" + registrationID);
        } else if ("flyme".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",flyme," + str;
            }
            PushLog.e(TAG, "flyme devid:" + registrationID);
        } else if ("fcm".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",fcm," + str;
            }
            PushLog.e(TAG, "firebase devid:" + registrationID);
        } else if ("oppo".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",oppo," + str;
            }
            PushLog.e(TAG, "oppo devid:" + registrationID);
        } else if ("vivo".equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + ",vivo," + str;
            }
            PushLog.e(TAG, "vivo devid:" + registrationID);
        } else if (PushConstantsImpl.HONOR.equals(serviceType)) {
            registrationID = com.netease.inner.pushclient.PushManager.getInstance().getRegistrationID(context, serviceType);
            if (!TextUtils.isEmpty(registrationID)) {
                registrationID = registrationID + "," + PushConstantsImpl.HONOR + "," + str;
            }
            PushLog.e(TAG, "honor devid:" + registrationID);
        }
        if (TextUtils.isEmpty(registrationID) && PushSetting.getCurUseNiepush(context, true)) {
            String devId = com.netease.inner.pushclient.PushManager.getInstance().getDevId(context);
            if (!TextUtils.isEmpty(devId) && s_multiPackSupport) {
                devId = devId + ",niepush," + str;
            }
            registrationID = devId;
            PushLog.e(TAG, "niepush devid:" + registrationID);
            PushLog.d(TAG, "s_multiPackSupport:" + s_multiPackSupport);
        }
        PushLog.e(TAG, "devid:" + registrationID);
        return registrationID;
    }

    public static String getDevId() {
        return getDevId(s_context);
    }

    public static String getToken() {
        if (!nieGetNewTokenSuccess) {
            Log.e(TAG, "getToken -> not nieGetNewTokenSuccess");
            return "";
        }
        return PushSetting.getVaule(s_context, "token");
    }

    public static void enableSound(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().enableSound(s_context, z);
        }
    }

    public static void enableVibrate(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().enableVibrate(s_context, z);
        }
    }

    public static void enableLight(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().enableLight(s_context, z);
        }
    }

    public static void enableRepeatProtect(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().enableRepeatProtect(s_context, z);
        }
    }

    public static void setRepeatProtectInterval(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().setRepeatProtectInterval(s_context, i);
        }
    }

    public static void setSenderID(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().setSenderID(s_context, str, str2);
        }
    }

    public static String getSenderID(String str) {
        return s_initialized ? com.netease.inner.pushclient.PushManager.getInstance().getSenderID(s_context, str) : "";
    }

    public static void setAppID(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().setAppID(s_context, str, str2);
        } else {
            CheckAppIdKeyUtil.recordSetAppId(str, str2);
        }
    }

    public static String getAppID(String str) {
        return s_initialized ? com.netease.inner.pushclient.PushManager.getInstance().getAppID(s_context, str) : "";
    }

    public static void setAppKey(String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().setAppKey(s_context, str, str2);
        } else {
            CheckAppIdKeyUtil.recordSetAppKey(str, str2);
        }
    }

    public static String getAppKey(String str) {
        return s_initialized ? com.netease.inner.pushclient.PushManager.getInstance().getAppKey(s_context, str) : "";
    }

    public static void setPushAddr(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setPushAddr, s_context:" + s_context + ", addr:" + str);
        Context context = s_context;
        if (context != null) {
            PushSetting.setPushAddr(context, str);
        }
    }

    public static String getSdkgate(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "PushSetting, sdkgate:" + PushSetting.getVaule(context, "sdkgate"));
        if (!TextUtils.isEmpty(PushSetting.getVaule(context, "sdkgate"))) {
            return PushSetting.getVaule(context, "sdkgate");
        }
        return sdkgate;
    }

    public static void setSdkgate(String str) {
        UploadUtil.setUrl(str);
        PushSetting.setKeyVaule(s_context, "sdkgate", str);
    }

    public static void setProductId(Context context, String str, String str2) {
        Log.d(TAG, "setProductId:" + str);
        try {
            product_id = str;
            client_key = str2;
            PushLog.d(TAG, "local product_id:" + PushSetting.getVaule(context, "product_id"));
            PushLog.d(TAG, "local client_key:" + PushSetting.getVaule(context, a.j));
            if (!product_id.equals(PushSetting.getVaule(context, "product_id")) || !client_key.equals(PushSetting.getVaule(context, a.j))) {
                PushSetting.setKeyVaule(context, "regetToken", "true");
                PushLog.d(TAG, "need to get new token");
            }
            PushSetting.setKeyVaule(context, "product_id", product_id);
            PushSetting.setKeyVaule(context, a.j, client_key);
            PushSetting.setKeyVaule(context, "hasSetProductId", "true");
            Log.d(TAG, "product_id:" + product_id);
            Log.d(TAG, "client_key:" + client_key);
            hasSetProductId = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNiePushAddr() {
        return PushSetting.getVaule(s_context, "niepushAddr");
    }

    public static void setNiePushAddr(String str) {
        PushSetting.setKeyVaule(s_context, "niepushAddr", str);
    }

    public static void setNiepushMode(int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setNiepushMode, s_context:" + s_context + ", mode:" + i);
        Context context = s_context;
        if (context != null) {
            PushSetting.setNiepushMode2(context, i);
        }
    }

    public static String getServiceType(Context context) {
        return com.netease.inner.pushclient.PushManager.getInstance().getServiceType(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setServiceType(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        com.netease.inner.pushclient.PushManager.getInstance().setServiceType(context, str);
    }

    private static void checkPushServiceType(Context context) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        PushLog.i(TAG, "checkPushServiceType");
        readConfig(context);
        boolean z9 = false;
        if (DeviceInfo.isMIUI(context) && usePlatform.get("miui").booleanValue()) {
            String appID = getAppID("miui");
            String appKey = getAppKey("miui");
            try {
                Class.forName("com.xiaomi.push.service.XMPushService");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(context, "miui", appID, appKey)) {
                    setRegid(null, "miui");
                }
                z8 = true;
            } catch (ClassNotFoundException e) {
                PushLog.d(TAG, "ClassNotFoundException:" + e.getMessage());
                z8 = false;
            }
            if (!TextUtils.isEmpty(appID) && !TextUtils.isEmpty(appKey) && z8) {
                setServiceType(context, "miui");
                return;
            }
        }
        if (DeviceInfo.isHuawei(context) && usePlatform.get("hms").booleanValue()) {
            String appID2 = getAppID("huawei");
            PushLog.d(TAG, "HMS appid:" + appID2);
            try {
                Class.forName("com.huawei.hms.support.api.push.PushReceiver");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(s_context, "hms", appID2, "")) {
                    setRegid(null, "hms");
                }
                z7 = true;
            } catch (ClassNotFoundException e2) {
                PushLog.d(TAG, "ClassNotFoundException:" + e2.getMessage());
                z7 = false;
            }
            if (!TextUtils.isEmpty(appID2) && z7) {
                PushLog.e(TAG, "2.5.2.300\u7248\u672c\u5f00\u59cb\uff0c\u5fc5\u987b\u628a\u534e\u4e3aappid\u914d\u7f6e\u5728androidmanifest#com.huawei.hms.client.appid\u91cc\u9762");
                setServiceType(context, "hms");
                return;
            }
        }
        if (DeviceInfo.isHuawei(context) && usePlatform.get("huawei").booleanValue()) {
            String appID3 = getAppID("huawei");
            try {
                Class.forName("com.huawei.android.pushagent.PushEventReceiver");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(context, "huawei", appID3, "")) {
                    setRegid(null, "huawei");
                }
                z6 = true;
            } catch (ClassNotFoundException e3) {
                PushLog.d(TAG, "ClassNotFoundException:" + e3.getMessage());
                z6 = false;
            }
            if (!TextUtils.isEmpty(appID3) && z6) {
                setServiceType(context, "huawei");
                return;
            }
        }
        if (OPPO.getInst().checkSupportOPPOPush(s_context) && usePlatform.get("oppo").booleanValue()) {
            String appID4 = getAppID("oppo");
            String appKey2 = getAppKey("oppo");
            try {
                Class.forName("com.netease.inner.pushclient.oppo.MessageReceiver");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(s_context, "oppo", appID4, appKey2)) {
                    setRegid(null, "oppo");
                }
                z5 = true;
            } catch (ClassNotFoundException e4) {
                PushLog.d(TAG, "ClassNotFoundException:" + e4.getMessage());
                z5 = false;
            }
            if (!TextUtils.isEmpty(appID4) && !TextUtils.isEmpty(appKey2) && z5) {
                setServiceType(context, "oppo");
                return;
            }
        }
        if (DeviceInfo.isFlyme(context) && usePlatform.get("flyme").booleanValue()) {
            String appID5 = getAppID("flyme");
            String appKey3 = getAppKey("flyme");
            try {
                Class.forName("com.meizu.cloud.pushsdk.NotificationService");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(context, "flyme", appID5, appKey3)) {
                    setRegid(null, "flyme");
                }
                z4 = true;
            } catch (ClassNotFoundException e5) {
                PushLog.d(TAG, "ClassNotFoundException:" + e5.getMessage());
                z4 = false;
            }
            if (!TextUtils.isEmpty(appID5) && z4) {
                setServiceType(context, "flyme");
                return;
            }
        }
        if (DeviceInfo.isVivo(context) && usePlatform.get("vivo").booleanValue()) {
            try {
                Class.forName("com.netease.inner.pushclient.vivo.MessageReceiver");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(s_context, "vivo", getAppID("vivo"), getAppKey("vivo"))) {
                    setRegid(null, "vivo");
                }
                z3 = true;
            } catch (ClassNotFoundException e6) {
                PushLog.d(TAG, "ClassNotFoundException:" + e6.getMessage());
                z3 = false;
            }
            if (z3) {
                setServiceType(context, "vivo");
                return;
            }
        }
        if (DeviceInfo.isHonor(context) && usePlatform.get(PushConstantsImpl.HONOR).booleanValue()) {
            try {
                Class.forName("com.netease.inner.pushclient.honor.HonorPushService");
                if (CheckAppIdKeyUtil.checkAppIdKeyChanged(s_context, PushConstantsImpl.HONOR, getAppID(PushConstantsImpl.HONOR), "")) {
                    setRegid(null, PushConstantsImpl.HONOR);
                }
                z2 = true;
            } catch (ClassNotFoundException e7) {
                PushLog.d(TAG, "ClassNotFoundException:" + e7.getMessage());
                z2 = false;
            }
            if (z2) {
                setServiceType(context, PushConstantsImpl.HONOR);
                return;
            }
        }
        int identifier = context.getResources().getIdentifier("default_web_client_id", ResIdReader.RES_TYPE_STRING, context.getPackageName());
        int identifier2 = context.getResources().getIdentifier("firebase_database_url", ResIdReader.RES_TYPE_STRING, context.getPackageName());
        try {
            Class.forName("com.google.firebase.iid.FirebaseInstanceIdReceiver");
            Class.forName("com.netease.inner.pushclient.firebase.MyFirebaseInstanceIDService");
            z = true;
        } catch (ClassNotFoundException e8) {
            PushLog.d(TAG, "ClassNotFoundException:" + e8.getMessage());
            z = false;
        }
        if (identifier != 0 && identifier2 != 0 && z && usePlatform.get("fcm").booleanValue()) {
            setServiceType(context, "fcm");
            return;
        }
        String senderID = getSenderID("gcm");
        try {
            Class.forName("com.google.android.gms.gcm.GoogleCloudMessaging");
            Class.forName("com.netease.inner.pushclient.gcm.PushClient");
            z9 = true;
        } catch (ClassNotFoundException e9) {
            PushLog.d(TAG, "ClassNotFoundException:" + e9.getMessage());
        }
        if (!TextUtils.isEmpty(senderID) && z9 && GCM.getInst().isGooglePlayServicesAvailable(context)) {
            setServiceType(context, "gcm");
        } else {
            setServiceType(context, "niepush");
        }
    }

    private static void readConfig(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "readConfig");
        setAppID("miui", "");
        setAppID("huawei", "");
        setAppID("hms", "");
        setAppID("flyme", "");
        setAppID("oppo", "");
        setAppID(PushConstantsImpl.HONOR, "");
        setAppID("vivo", "");
        setAppKey("miui", "");
        setAppKey("huawei", "");
        setAppKey("hms", "");
        setAppKey("flyme", "");
        setAppKey("oppo", "");
        setAppKey("vivo", "");
        setSenderID("gcm", "");
        try {
            readJsonConfig(context);
            readMetadataConfig(context);
        } catch (Exception unused) {
        }
        CheckAppIdKeyUtil.setCustomAppIdKey(context);
    }

    private static void readMetadataConfig(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Bundle bundle;
        PushLog.i(TAG, "readMetadataConfig");
        try {
            bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
        } catch (Exception e) {
            PushLog.e(TAG, "Failed to load meta-data, err:" + e.toString());
            bundle = null;
        }
        if (bundle == null) {
            return;
        }
        String configMetadata = readConfigMetadata(bundle, "ngpush_miui_appid");
        if (!TextUtils.isEmpty(configMetadata)) {
            setAppID("miui", configMetadata);
        }
        String configMetadata2 = readConfigMetadata(bundle, "ngpush_miui_appkey");
        if (!TextUtils.isEmpty(configMetadata2)) {
            setAppKey("miui", configMetadata2);
        }
        String configMetadata3 = readConfigMetadata(bundle, "ngpush_huawei_appid");
        if (!TextUtils.isEmpty(configMetadata3)) {
            setAppID("huawei", configMetadata3);
            setAppID("hms", configMetadata3);
        } else {
            String configMetadata4 = readConfigMetadata(bundle, Constants.HUAWEI_HMS_CLIENT_APPID);
            if (!TextUtils.isEmpty(configMetadata4)) {
                setAppID("huawei", configMetadata4);
                setAppID("hms", configMetadata4);
            }
        }
        String configMetadata5 = readConfigMetadata(bundle, "ngpush_flyme_appid");
        if (!TextUtils.isEmpty(configMetadata5)) {
            setAppID("flyme", configMetadata5);
        }
        String configMetadata6 = readConfigMetadata(bundle, "ngpush_flyme_appkey");
        if (!TextUtils.isEmpty(configMetadata6)) {
            setAppKey("flyme", configMetadata6);
        }
        String configMetadata7 = readConfigMetadata(bundle, "ngpush_gcm_senderid");
        if (!TextUtils.isEmpty(configMetadata7)) {
            setSenderID("gcm", configMetadata7);
        }
        String configMetadata8 = readConfigMetadata(bundle, "ngpush_oppo_appid");
        if (!TextUtils.isEmpty(configMetadata8)) {
            setAppID("oppo", configMetadata8);
        }
        String configMetadata9 = readConfigMetadata(bundle, "ngpush_oppo_appkey");
        if (!TextUtils.isEmpty(configMetadata9)) {
            setAppKey("oppo", configMetadata9);
        }
        String configMetadata10 = readConfigMetadata(bundle, "ngpush_" + PushConstantsImpl.HONOR + "_appid");
        if (!TextUtils.isEmpty(configMetadata10)) {
            setAppID(PushConstantsImpl.HONOR, configMetadata10);
        } else {
            String configMetadata11 = readConfigMetadata(bundle, "com.hihonor.push.app_id");
            if (!TextUtils.isEmpty(configMetadata11)) {
                setAppID(PushConstantsImpl.HONOR, configMetadata11);
            }
        }
        String configMetadata12 = readConfigMetadata(bundle, "com.vivo.push.app_id");
        if (!TextUtils.isEmpty(configMetadata12)) {
            setAppID("vivo", configMetadata12);
        }
        String configMetadata13 = readConfigMetadata(bundle, "com.vivo.push.api_key");
        if (TextUtils.isEmpty(configMetadata13)) {
            return;
        }
        setAppKey("vivo", configMetadata13);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String readConfigMetadata(android.os.Bundle r5, java.lang.String r6) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            java.lang.String r0 = ""
            java.lang.Object r5 = r5.get(r6)     // Catch: java.lang.Exception -> L21
            if (r5 != 0) goto L9
            return r0
        L9:
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch: java.lang.Exception -> L21
            java.lang.String r1 = "ngpush_default"
            boolean r1 = r1.equalsIgnoreCase(r5)     // Catch: java.lang.Exception -> L1f
            if (r1 != 0) goto L1d
            java.lang.String r1 = "null"
            boolean r1 = r1.equalsIgnoreCase(r5)     // Catch: java.lang.Exception -> L1f
            if (r1 == 0) goto L47
        L1d:
            r5 = r0
            goto L47
        L1f:
            r0 = move-exception
            goto L25
        L21:
            r5 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
        L25:
            java.lang.String r1 = com.netease.pushclient.PushManagerImpl.TAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Failed to load meta-data "
            r2.append(r3)
            r2.append(r6)
            java.lang.String r3 = ", err:"
            r2.append(r3)
            java.lang.String r0 = r0.toString()
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            com.netease.push.utils.PushLog.e(r1, r0)
        L47:
            java.lang.String r0 = com.netease.pushclient.PushManagerImpl.TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "readConfigMetadata, name:"
            r1.append(r2)
            r1.append(r6)
            java.lang.String r6 = ", value:"
            r1.append(r6)
            r1.append(r5)
            java.lang.String r6 = r1.toString()
            com.netease.push.utils.PushLog.i(r0, r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushclient.PushManagerImpl.readConfigMetadata(android.os.Bundle, java.lang.String):java.lang.String");
    }

    private static void readJsonConfig(Context context) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "readJsonConfig");
        String packageName = context.getPackageName();
        String str = packageName + ".ngpush.miui";
        String configFile = readConfigFile(context, str);
        if (configFile != null) {
            try {
                JSONObject jSONObject = new JSONObject(configFile);
                String strOptString = jSONObject.optString(ConstProp.APPID);
                if (!TextUtils.isEmpty(strOptString)) {
                    setAppID("miui", strOptString);
                }
                String strOptString2 = jSONObject.optString("APPKEY");
                if (!TextUtils.isEmpty(strOptString2)) {
                    setAppKey("miui", strOptString2);
                }
            } catch (Exception e) {
                PushLog.e(TAG, "parse config file:" + str + ", err:" + e);
            }
        }
        String str2 = packageName + ".ngpush.huawei";
        String configFile2 = readConfigFile(context, str2);
        if (configFile2 != null) {
            try {
                String strOptString3 = new JSONObject(configFile2).optString(ConstProp.APPID);
                if (!TextUtils.isEmpty(strOptString3)) {
                    setAppID("huawei", strOptString3);
                    setAppID("hms", strOptString3);
                }
            } catch (Exception e2) {
                PushLog.e(TAG, "parse config file:" + str2 + ", err:" + e2.getMessage());
            }
        }
        String str3 = packageName + ".ngpush.gcm";
        String configFile3 = readConfigFile(context, str3);
        if (configFile3 != null) {
            try {
                String strOptString4 = new JSONObject(configFile3).optString("SENDERID");
                if (!TextUtils.isEmpty(strOptString4)) {
                    setSenderID("gcm", strOptString4);
                }
            } catch (Exception e3) {
                PushLog.e(TAG, "parse config file:" + str3 + ", err:" + e3.getMessage());
            }
        }
        String str4 = packageName + ".ngpush.honor";
        String configFile4 = readConfigFile(context, str4);
        if (configFile4 != null) {
            try {
                String strOptString5 = new JSONObject(configFile4).optString(ConstProp.APPID);
                if (TextUtils.isEmpty(strOptString5)) {
                    return;
                }
                setAppID(PushConstantsImpl.HONOR, strOptString5);
            } catch (Exception e4) {
                PushLog.e(TAG, "parse config file:" + str4 + ", err:" + e4.getMessage());
            }
        }
    }

    private static String readConfigFile(Context context, String str) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        try {
            InputStream inputStreamOpen = context.getAssets().open(str, 3);
            int iAvailable = inputStreamOpen.available();
            if (iAvailable <= 0) {
                return null;
            }
            byte[] bArr = new byte[iAvailable];
            inputStreamOpen.read(bArr);
            return new String(bArr, "UTF-8");
        } catch (Exception e) {
            PushLog.w(TAG, "config file not found:" + str + ", err:" + e.toString());
            return null;
        }
    }

    private static boolean hasPermissionDeclared(Context context, String str) throws IllegalAccessException, PackageManager.NameNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096);
            String[] strArr = packageInfo != null ? packageInfo.requestedPermissions : null;
            if (strArr != null && strArr.length > 0) {
                for (String str2 : strArr) {
                    if (str.equalsIgnoreCase(str2)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            PushLog.e(TAG, "hasPermissionDeclared exception:" + e.toString());
        }
        return false;
    }

    public static class TaskSubmitter {
        final ExecutorService m_executorService = Executors.newSingleThreadExecutor();

        public Future submit(Runnable runnable) {
            if (this.m_executorService.isTerminated() || this.m_executorService.isShutdown() || runnable == null) {
                return null;
            }
            return this.m_executorService.submit(runnable);
        }

        public void shutdown() {
            this.m_executorService.shutdown();
        }
    }

    public static void clearContext() {
        if (s_context != null) {
            s_context = null;
        }
    }

    public static void goToNotificationSetting() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.w(TAG, "checkNotifySetting:" + checkNotifySetting());
        try {
            if (s_context == null || checkNotifySetting()) {
                return;
            }
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 26) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", s_context.getPackageName());
            } else if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 26) {
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", s_context.getPackageName());
                intent.putExtra("app_uid", s_context.getApplicationInfo().uid);
            } else if (Build.VERSION.SDK_INT == 19) {
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + s_context.getPackageName()));
            } else {
                intent.addFlags(268435456);
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", s_context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction("android.intent.action.VIEW");
                    intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", s_context.getPackageName());
                }
            }
            intent.addFlags(268435456);
            s_context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkNotifySetting() throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            if (s_context == null) {
                return false;
            }
            boolean zAreNotificationsEnabled = NotificationManagerCompat.from(s_context).areNotificationsEnabled();
            PushLog.d(TAG, "checkNotifySetting -> notificationsEnabled: " + zAreNotificationsEnabled);
            return zAreNotificationsEnabled;
        } catch (Exception e) {
            PushLog.e(TAG, "checkNotifySetting -> e: " + e);
            if (s_context != null) {
                PushLog.w(TAG, "isNotificationEnabled!!!!");
                if (Build.VERSION.SDK_INT >= 26 && ((NotificationManager) s_context.getSystemService("notification")).getImportance() == 0) {
                    return false;
                }
                if (Build.VERSION.SDK_INT < 19) {
                    return true;
                }
                PushLog.w(TAG, "isNotificationEnabled2!!!!");
                AppOpsManager appOpsManager = (AppOpsManager) s_context.getSystemService("appops");
                ApplicationInfo applicationInfo = s_context.getApplicationInfo();
                String packageName = s_context.getApplicationContext().getPackageName();
                int i = applicationInfo.uid;
                try {
                    Class<?> cls = Class.forName(AppOpsManager.class.getName());
                    return ((Integer) cls.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, Integer.valueOf(((Integer) cls.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(i), packageName)).intValue() == 0;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }

    public static boolean checkNotifySettingContext(Context context) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        if (context != null) {
            PushLog.w(TAG, "isNotificationEnabled!!!!");
            if (Build.VERSION.SDK_INT >= 26 && ((NotificationManager) context.getSystemService("notification")).getImportance() == 0) {
                return false;
            }
            if (Build.VERSION.SDK_INT < 19) {
                return true;
            }
            PushLog.w(TAG, "isNotificationEnabled2!!!!");
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            String packageName = context.getApplicationContext().getPackageName();
            int i = applicationInfo.uid;
            try {
                Class<?> cls = Class.forName(AppOpsManager.class.getName());
                return ((Integer) cls.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, Integer.valueOf(((Integer) cls.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)).intValue()), Integer.valueOf(i), packageName)).intValue() == 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void createPushChannel(String str, String str2, String str3, String str4, boolean z, boolean z2, boolean z3, String str5) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (Build.VERSION.SDK_INT >= 26) {
            PushLog.d(TAG, "createPushChannel");
            NotificationManager notificationManager = (NotificationManager) s_context.getSystemService("notification");
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup(str, str2));
            NotificationChannel notificationChannel = new NotificationChannel(str3, str4, 4);
            notificationChannel.setGroup(str);
            if (z3 && str5 != null) {
                notificationChannel.setSound(Uri.parse(str5), new AudioAttributes.Builder().setContentType(4).setUsage(6).build());
            }
            if (z2) {
                notificationChannel.enableLights(z);
            }
            if (z) {
                notificationChannel.enableVibration(z);
            }
            notificationManager.createNotificationChannel(notificationChannel);
            return;
        }
        PushLog.d(TAG, "Build.VERSION.SDK_INT < Build.VERSION_CODES.O, createPushChannel fail");
    }

    public static void ntlogin() {
        new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.4
            @Override // java.lang.Runnable
            public void run() throws JSONException {
                JSONObject jSONObject = new JSONObject();
                try {
                    Log.i(PushManagerImpl.TAG, "unisdk_deviceid:" + UnisdkDeviceUtil.getUnisdkDeviceId(PushManagerImpl.s_context, null));
                    jSONObject.put("udid", UnisdkDeviceUtil.getAndroidId(PushManagerImpl.s_context));
                    jSONObject.put("regid", PushManagerImpl.getDevId());
                    jSONObject.put("system_language", PushManagerImpl.getSystemLanguage());
                    jSONObject.put("timezone", UnisdkDeviceUtil.getTimeZone());
                    jSONObject.put("sdkversion", PushConstantsImpl.SDK_VERSION);
                    jSONObject.put("unisdk_deviceid", UnisdkDeviceUtil.getUnisdkDeviceId(PushManagerImpl.s_context, null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String str = PushManagerImpl.gate + "/ntlogin";
                if (TextUtils.isEmpty(PushManagerImpl.gate) || TextUtils.isEmpty(PushManagerImpl.gateKey) || TextUtils.isEmpty(PushManagerImpl.project) || TextUtils.isEmpty(jSONObject.optString("regid")) || TextUtils.isEmpty(jSONObject.optString("udid")) || TextUtils.isEmpty(jSONObject.optString("unisdk_deviceid"))) {
                    Log.i(PushManagerImpl.TAG, "request param is null");
                } else {
                    PushManagerImpl.SendRequest(str, "/ntlogin", jSONObject, PushManagerImpl.gateKey);
                }
            }
        }).start();
    }

    public static void subscribe(String str, String str2, String str3, String str4, String str5) {
        service = str;
        aid = str2;
        sdkuid = str3;
        roleid = str4;
        subscriber = str5;
        new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.5
            @Override // java.lang.Runnable
            public void run() throws JSONException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                JSONObject jSONObject = new JSONObject();
                try {
                    Log.i(PushManagerImpl.TAG, "unisdk_deviceid:" + UnisdkDeviceUtil.getUnisdkDeviceId(PushManagerImpl.s_context, null));
                    jSONObject.put(NotificationCompat.CATEGORY_SERVICE, PushManagerImpl.service);
                    jSONObject.put("project", PushManagerImpl.project);
                    jSONObject.put("udid", UnisdkDeviceUtil.getAndroidId(PushManagerImpl.s_context));
                    jSONObject.put("regid", PushManagerImpl.getDevId());
                    jSONObject.put("aid", PushManagerImpl.aid);
                    jSONObject.put("sdkuid", PushManagerImpl.sdkuid);
                    jSONObject.put(Const.CONFIG_KEY.ROLEID, PushManagerImpl.roleid);
                    jSONObject.put("subscriber", PushManagerImpl.subscriber);
                    jSONObject.put("system_language", PushManagerImpl.getSystemLanguage());
                    jSONObject.put("timezone", UnisdkDeviceUtil.getTimeZone());
                    jSONObject.put("sdkversion", PushConstantsImpl.SDK_VERSION);
                    jSONObject.put("unisdk_deviceid", UnisdkDeviceUtil.getUnisdkDeviceId(PushManagerImpl.s_context, null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(PushManagerImpl.TAG, "service:" + PushManagerImpl.service);
                Log.i(PushManagerImpl.TAG, "project:" + PushManagerImpl.project);
                Log.i(PushManagerImpl.TAG, "udid:" + UnisdkDeviceUtil.getAndroidId(PushManagerImpl.s_context));
                Log.i(PushManagerImpl.TAG, "regid:" + PushManagerImpl.getDevId());
                Log.i(PushManagerImpl.TAG, "aid:" + PushManagerImpl.aid);
                Log.i(PushManagerImpl.TAG, "sdkuid:" + PushManagerImpl.sdkuid);
                Log.i(PushManagerImpl.TAG, "roleid:" + PushManagerImpl.roleid);
                Log.i(PushManagerImpl.TAG, "subscriber:" + PushManagerImpl.subscriber);
                Log.i(PushManagerImpl.TAG, "system_language:" + PushManagerImpl.getSystemLanguage());
                Log.i(PushManagerImpl.TAG, "unisdk_deviceid:" + UnisdkDeviceUtil.getUnisdkDeviceId(PushManagerImpl.s_context, null));
                String str6 = PushManagerImpl.gate + "/subscribe";
                if (TextUtils.isEmpty(PushManagerImpl.gate) || TextUtils.isEmpty(PushManagerImpl.gateKey) || TextUtils.isEmpty(PushManagerImpl.project) || TextUtils.isEmpty(jSONObject.optString("regid")) || TextUtils.isEmpty(jSONObject.optString("udid")) || TextUtils.isEmpty(jSONObject.optString("unisdk_deviceid"))) {
                    Log.i(PushManagerImpl.TAG, "request param is null");
                    return;
                }
                String strSendRequest = PushManagerImpl.SendRequest(str6, "/subscribe", jSONObject, PushManagerImpl.gateKey);
                PushLog.i(PushManagerImpl.TAG, "subscribe Finish");
                try {
                    Class.forName("com.netease.pushclient.PushManager").getMethod("subscribeFinish", String.class).invoke(null, strSendRequest);
                } catch (Exception e2) {
                    PushLog.e(PushManagerImpl.TAG, e2.toString());
                }
            }
        }).start();
    }

    public static void bindAccount(String str, Boolean bool, Boolean bool2) throws JSONException {
        if (!nieLoginSuccess) {
            Log.e(TAG, "bindAccount -> not nieLoginSuccess");
            return;
        }
        subscriber = str;
        final JSONObject jSONObject = new JSONObject();
        try {
            PushSetting.setKeyVaule(s_context, "account", str);
            jSONObject.put("account", str);
            jSONObject.put("unbind", bool);
            jSONObject.put("cover", bool2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.6
            @Override // java.lang.Runnable
            public void run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                try {
                    String string = jSONObject.toString();
                    PushLog.d(PushManagerImpl.TAG, "requestBody=" + string);
                    long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
                    new StringBuffer().append(string);
                    HashMap map = new HashMap();
                    map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                    map.put("token", PushSetting.getVaule(PushManagerImpl.s_context, "token") + "");
                    map.put("access_key", PushSetting.getVaule(PushManagerImpl.s_context, "access_key") + "");
                    PushLog.d(PushManagerImpl.TAG, "token=" + ((String) map.get("token")));
                    PushLog.d(PushManagerImpl.TAG, "access_key=" + ((String) map.get("access_key")));
                    String strDoHttp = NetUtil.doHttp(PushManagerImpl.getSdkgate(PushManagerImpl.s_context) + "/user/bind_token", "POST", string, null, map);
                    PushLog.d(PushManagerImpl.TAG, "content=" + strDoHttp);
                    if (new JSONObject(strDoHttp).optInt("code") == 0) {
                        Log.d(PushManagerImpl.TAG, "bindAccount success");
                    }
                } catch (Exception e2) {
                    PushLog.d(PushManagerImpl.TAG, "err=" + e2);
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    private static String registerToken(String str, String str2, String str3, String str4, String str5) throws JSONException {
        Log.i(TAG, "registerToken");
        Log.d(TAG, "url=" + getSdkgate(s_context));
        type = str3;
        final JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("product_id", str);
            jSONObject.put(a.j, str2);
            jSONObject.put("channel", str3);
            jSONObject.put(OneTrack.Param.PKG, str4);
            jSONObject.put("regid", str5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String string = jSONObject.toString();
                    Log.d(PushManagerImpl.TAG, "requestBody=" + string);
                    long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
                    new StringBuffer().append(string);
                    HashMap map = new HashMap();
                    map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                    String strDoHttp = NetUtil.doHttp(PushManagerImpl.getSdkgate(PushManagerImpl.s_context) + "/token/register", "POST", string, null, map);
                    Log.d(PushManagerImpl.TAG, "content=" + strDoHttp);
                    JSONObject jSONObject2 = new JSONObject(strDoHttp);
                    if (jSONObject2.optInt("code") == 0) {
                        String unused = PushManagerImpl.token = jSONObject2.optJSONObject("data").optString("token");
                        String unused2 = PushManagerImpl.access_key = jSONObject2.optJSONObject("data").optString("access_key");
                        PushSetting.setKeyVaule(PushManagerImpl.s_context, "token", PushManagerImpl.token);
                        PushSetting.setKeyVaule(PushManagerImpl.s_context, "access_key", PushManagerImpl.access_key);
                        PushSetting.setKeyVaule(PushManagerImpl.s_context, "type", PushManagerImpl.type);
                        PushLog.d(PushManagerImpl.TAG, "sendBroadcast Token to Mpay");
                        try {
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(PushManagerImpl.s_context);
                            Intent intent = new Intent(PushConstantsImpl.CLIENT_ACTION_MPAY_MESSAGE);
                            intent.setPackage(PushManagerImpl.s_context.getPackageName());
                            intent.putExtra("type", "token");
                            intent.putExtra("token", PushManagerImpl.token);
                            localBroadcastManager.sendBroadcast(intent);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception e3) {
                    Log.d(PushManagerImpl.TAG, "err=" + e3);
                    e3.printStackTrace();
                }
            }
        }).start();
        return "";
    }

    public static String find() {
        try {
            Log.i(TAG, "find");
            Log.d(TAG, "url=" + getSdkgate(s_context));
            Log.i(TAG, "token:" + PushSetting.getVaule(s_context, "token"));
            Log.i(TAG, "access_key:" + PushSetting.getVaule(s_context, "access_key"));
            Log.i(TAG, "AreaZone:" + UnisdkDeviceUtil.getAreaZone());
            final String vaule = PushSetting.getVaule(s_context, "token");
            final String vaule2 = PushSetting.getVaule(s_context, "access_key");
            final String sdkgate2 = getSdkgate(s_context);
            final JSONObject jSONObject = new JSONObject();
            new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.8
                @Override // java.lang.Runnable
                public void run() throws JSONException {
                    try {
                        String string = jSONObject.toString();
                        Log.d(PushManagerImpl.TAG, "requestBody=" + string);
                        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                        new StringBuffer().append(string);
                        HashMap map = new HashMap();
                        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                        map.put("timestamp", iCurrentTimeMillis + "");
                        map.put("token", vaule);
                        map.put("access_key", vaule2);
                        String strDoHttp = NetUtil.doHttp(sdkgate2 + "/token/find?time_zone=" + UnisdkDeviceUtil.getAreaZone(), "GET", string, null, map);
                        Log.d(PushManagerImpl.TAG, "content=" + strDoHttp);
                        JSONObject jSONObject2 = new JSONObject(strDoHttp);
                        if (jSONObject2.optInt("code") == 0) {
                            Log.d(PushManagerImpl.TAG, "findTokenResult=" + jSONObject2.optJSONObject("data"));
                            Log.d(PushManagerImpl.TAG, "find success");
                            JSONObject jSONObject3 = new JSONObject();
                            jSONObject3.put("methodId", "find");
                            jSONObject3.put("result", jSONObject2.optJSONObject("data"));
                            PushClientReceiverImpl._onFunctionCallBack(jSONObject3.toString());
                        }
                    } catch (Exception e) {
                        Log.d(PushManagerImpl.TAG, "err=" + e);
                        e.printStackTrace();
                    }
                }
            }).start();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setFeature(final boolean z, final boolean z2, final String str) throws JSONException {
        try {
            if (!nieLoginSuccess) {
                Log.e(TAG, "setFeature -> not nieLoginSuccess");
                return;
            }
            Log.i(TAG, "setFeature");
            Log.d(TAG, "url=" + getSdkgate(s_context));
            Log.i(TAG, "token:" + PushSetting.getVaule(s_context, "token"));
            Log.i(TAG, "access_key:" + PushSetting.getVaule(s_context, "access_key"));
            final String vaule = PushSetting.getVaule(s_context, "token");
            final String vaule2 = PushSetting.getVaule(s_context, "access_key");
            final String sdkgate2 = getSdkgate(s_context);
            String vaule3 = PushSetting.getVaule(s_context, b.n);
            String vaule4 = PushSetting.getVaule(s_context, "cover");
            String vaule5 = PushSetting.getVaule(s_context, "unset");
            if (Boolean.toString(z).equals(vaule4) && Boolean.toString(z2).equals(vaule5) && vaule3.equals(str)) {
                Log.i(TAG, "same features!!");
                return;
            }
            final JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject(str);
            jSONObject.put("cover", z);
            jSONObject.put("features", jSONObject2);
            jSONObject.put("token", vaule);
            jSONObject.put("unset", z2);
            new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.9
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        String string = jSONObject.toString();
                        Log.d(PushManagerImpl.TAG, "requestBody=" + string);
                        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                        new StringBuffer().append(string);
                        HashMap map = new HashMap();
                        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                        map.put("timestamp", iCurrentTimeMillis + "");
                        map.put("token", vaule);
                        map.put("access_key", vaule2);
                        String strDoHttp = NetUtil.doHttp(sdkgate2 + "/token/set_feature", "POST", string, null, map);
                        Log.d(PushManagerImpl.TAG, "content=" + strDoHttp);
                        if (new JSONObject(strDoHttp).optInt("code") == 0) {
                            Log.d(PushManagerImpl.TAG, "setFeature success");
                            PushSetting.setKeyVaule(PushManagerImpl.s_context, b.n, str);
                            PushSetting.setKeyVaule(PushManagerImpl.s_context, "cover", Boolean.toString(z));
                            PushSetting.setKeyVaule(PushManagerImpl.s_context, "unset", Boolean.toString(z2));
                        }
                    } catch (Exception e) {
                        Log.d(PushManagerImpl.TAG, "err=" + e);
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRefuseTime(final String str, final String str2) throws JSONException {
        try {
            if (!nieLoginSuccess) {
                Log.e(TAG, "setRefuseTime -> not nieLoginSuccess");
                return;
            }
            Log.i(TAG, "setRefuseTime");
            Log.d(TAG, "url=" + getSdkgate(s_context));
            Log.i(TAG, "token:" + PushSetting.getVaule(s_context, "token"));
            Log.i(TAG, "access_key:" + PushSetting.getVaule(s_context, "access_key"));
            final String vaule = PushSetting.getVaule(s_context, "token");
            final String vaule2 = PushSetting.getVaule(s_context, "access_key");
            final String sdkgate2 = getSdkgate(s_context);
            String vaule3 = PushSetting.getVaule(s_context, "time_zone");
            String vaule4 = PushSetting.getVaule(s_context, "refuse_times");
            if (str.equals(vaule3) && str2.equals(vaule4)) {
                Log.i(TAG, "timeZone have been set!!");
                return;
            }
            final JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray(str2);
            jSONObject.put("time_zone", str);
            jSONObject.put("refuse_times", jSONArray);
            jSONObject.put("token", vaule);
            new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.10
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        String string = jSONObject.toString();
                        Log.d(PushManagerImpl.TAG, "requestBody=" + string);
                        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                        new StringBuffer().append(string);
                        HashMap map = new HashMap();
                        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                        map.put("timestamp", iCurrentTimeMillis + "");
                        map.put("token", vaule);
                        map.put("access_key", vaule2);
                        String strDoHttp = NetUtil.doHttp(sdkgate2 + "/token/set_refuse_time", "POST", string, null, map);
                        Log.d(PushManagerImpl.TAG, "content=" + strDoHttp);
                        if (new JSONObject(strDoHttp).optInt("code") == 0) {
                            Log.d(PushManagerImpl.TAG, "setRefuseTime success");
                            PushSetting.setKeyVaule(PushManagerImpl.s_context, "time_zone", str);
                            PushSetting.setKeyVaule(PushManagerImpl.s_context, "refuse_times", str2);
                        }
                    } catch (Exception e) {
                        Log.d(PushManagerImpl.TAG, "err=" + e);
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRegid(String str, final String str2) {
        try {
            Log.i(TAG, "setRegid");
            Log.d(TAG, "url=" + getSdkgate(s_context));
            Log.i(TAG, "regid:" + str);
            Log.i(TAG, "token:" + PushSetting.getVaule(s_context, "token"));
            Log.i(TAG, "access_key:" + PushSetting.getVaule(s_context, "access_key"));
            Log.i(TAG, "old_regid:" + PushSetting.getVaule(s_context, "old_regid"));
            PushSetting.setKeyVaule(s_context, "regid", str == null ? "" : str);
            final String vaule = PushSetting.getVaule(s_context, "token");
            final String vaule2 = PushSetting.getVaule(s_context, "access_key");
            final String sdkgate2 = getSdkgate(s_context);
            if (TextUtils.isEmpty(vaule)) {
                Log.d(TAG, "regid or token is null");
                return;
            }
            final JSONObject jSONObject = new JSONObject();
            jSONObject.put("regid", str);
            jSONObject.put("channel", str2);
            new Thread(new Runnable() { // from class: com.netease.pushclient.PushManagerImpl.11
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        String string = jSONObject.toString();
                        Log.d(PushManagerImpl.TAG, "requestBody=" + string);
                        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                        new StringBuffer().append(string);
                        HashMap map = new HashMap();
                        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
                        map.put("timestamp", iCurrentTimeMillis + "");
                        map.put("token", vaule);
                        map.put("access_key", vaule2);
                        String strDoHttp = NetUtil.doHttp(sdkgate2 + "/token/set_regid", "POST", string, null, map);
                        Log.d(PushManagerImpl.TAG, "content=" + strDoHttp);
                        if (new JSONObject(strDoHttp).optInt("code") == 0) {
                            Log.d(PushManagerImpl.TAG, "setRegid success");
                            PushSetting.setKeyVaule(PushManagerImpl.s_context, "old_regid", PushSetting.getVaule(PushManagerImpl.s_context, "regid"));
                            CheckAppIdKeyUtil.saveEffectiveAppIdKey(PushManagerImpl.s_context, str2);
                        }
                    } catch (Exception e) {
                        Log.d(PushManagerImpl.TAG, "err=" + e);
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String SendRequest(String str, String str2, JSONObject jSONObject, String str3) {
        Log.i(TAG, "SendRequest");
        Log.d(TAG, "url=" + str);
        try {
            String string = jSONObject.toString();
            Log.d(TAG, "requestBody=" + string);
            int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(string);
            stringBuffer.append(iCurrentTimeMillis);
            stringBuffer.append(str3);
            Log.d(TAG, "sb=" + stringBuffer.toString());
            String strString2MD5 = string2MD5(stringBuffer.toString());
            Log.d(TAG, "strAuth=" + strString2MD5);
            HashMap map = new HashMap();
            map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
            map.put("timestamp", iCurrentTimeMillis + "");
            map.put("project_code", project);
            map.put("platform", "android");
            map.put(com.alipay.sdk.m.k.b.n, strString2MD5);
            String strDoHttp = NetUtil.doHttp(str, "POST", string, null, map);
            Log.d(TAG, "content=" + strDoHttp);
            return strDoHttp;
        } catch (Exception e) {
            Log.d(TAG, "err=" + e);
            e.printStackTrace();
            return "";
        }
    }

    public static String string2MD5(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                bArr[i] = (byte) charArray[i];
            }
            byte[] bArrDigest = messageDigest.digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArrDigest) {
                int i2 = b & 255;
                if (i2 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i2));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
    }

    public static void setNotUsePushPlatform(String str, boolean z) {
        if ("miui".equals(str)) {
            usePlatform.replace("miui", Boolean.valueOf(z));
            return;
        }
        if ("huawei".equals(str)) {
            usePlatform.replace("huawei", Boolean.valueOf(z));
            return;
        }
        if ("hms".equals(str)) {
            usePlatform.replace("hms", Boolean.valueOf(z));
            return;
        }
        if ("oppo".equals(str)) {
            usePlatform.replace("oppo", Boolean.valueOf(z));
            return;
        }
        if ("vivo".equals(str)) {
            usePlatform.replace("vivo", Boolean.valueOf(z));
        } else if ("flyme".equals(str)) {
            usePlatform.replace("flyme", Boolean.valueOf(z));
        } else if ("fcm".equals(str)) {
            usePlatform.replace("fcm", Boolean.valueOf(z));
        }
    }

    public static String getSystemLanguage() {
        Locale locale;
        String str = "";
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
            if (locale != null) {
                str = locale.getLanguage() + "_" + locale.getCountry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "StrUtil [getSystemLanguage] final System Language=" + str);
        return str;
    }

    public static void reportClickNotification(Context context, String str, String str2, String str3) throws JSONException {
        Log.i(TAG, "context=" + context + ",push_id=" + str + ",plan_id=" + str2);
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ngpushDeviceId=");
        sb.append(getDevId(context));
        Log.i(str4, sb.toString());
        Log.i(TAG, "packageName=" + context.getPackageName());
        Log.i(TAG, "registerId=" + getDevId(context).split(",")[0]);
        Log.i(TAG, "extendJson=" + getDevId(context).split(",")[0]);
        Log.i(TAG, "token=" + PushSetting.getVaule(context, "token"));
        Log.i(TAG, "FilesDir=" + context.getFilesDir());
        Log.i(TAG, "package=" + context.getPackageName());
        if ("".equals(str) || str == null || "".equals(str2) || str2 == null) {
            return;
        }
        try {
            if (!"niepush".equals(str3)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("event_type", "receive");
                jSONObject.put("plan_id", str2);
                jSONObject.put("push_id", str);
                jSONObject.put("regid", PushSetting.getVaule(context, "regid"));
                jSONObject.put("receive_channel", str3);
                jSONObject.put("account", PushSetting.getVaule(context, "account"));
                UploadUtil.SendRequest(jSONObject, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("event_type", "click");
            jSONObject2.put("plan_id", str2);
            jSONObject2.put("push_id", str);
            jSONObject2.put("regid", PushSetting.getVaule(context, "regid"));
            jSONObject2.put("receive_channel", str3);
            jSONObject2.put("account", PushSetting.getVaule(context, "account"));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        UploadUtil.SendRequest(jSONObject2, context);
    }

    public static void setPermissionTips(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            PushLog.i(TAG, "setPermissionTips:" + str);
            JSONObject jSONObject = new JSONObject(str);
            permissionTips = jSONObject.optString("permissionTips");
            goToPermissionTips = jSONObject.optString("goToPermissionTips");
            refuseTips = jSONObject.optString("refuseTips");
            PushLog.i(TAG, "permissionTips:" + permissionTips);
            PushLog.i(TAG, "goToPermissionTips:" + goToPermissionTips);
            PushLog.i(TAG, "refuseTips:" + refuseTips);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setIsShowDefualtDialog(boolean z) {
        showPermissionDialog = z;
    }

    public static void autoClickReport(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String strOptString;
        JSONObject jSONObject;
        String plan_id;
        String strOptString2;
        JSONObject jSONObjectOptJSONObject;
        JSONObject jSONObjectOptJSONObject2;
        JSONObject jSONObject2;
        PushLog.i(TAG, "getFrom, intent:" + intent);
        PushLog.i(TAG, "getFrom, intent Extra:" + intent.getExtras());
        try {
            Bundle extras = intent.getExtras();
            String string = extras.getString("service_type");
            PushLog.i(TAG, "service_type:" + string);
            String serviceType = getServiceType(context);
            if (TextUtils.isEmpty(string)) {
                string = serviceType;
            }
            PushLog.i(TAG, "receive_channel:" + string);
            String strOptString3 = "";
            if ("niepush".equals(string)) {
                strOptString3 = extras.getString("push_id");
                plan_id = extras.getString("plan_id");
            } else if ("miui".equals(string)) {
                NotifyMessageImpl from = NotifyMessageImpl.getFrom(intent);
                strOptString3 = from.getPush_id();
                plan_id = from.getPlan_id();
            } else {
                if ("huawei".equals(string) || "hms".equals(string) || PushConstantsImpl.HONOR.equals(string)) {
                    String string2 = extras.getString("system_content");
                    PushLog.i(TAG, "content:" + string2);
                    new JSONObject();
                    try {
                        jSONObject = new JSONObject(string2);
                        strOptString = jSONObject.optString("push_id");
                    } catch (JSONException e) {
                        e = e;
                        strOptString = "";
                    }
                    try {
                        strOptString3 = jSONObject.optString("plan_id");
                    } catch (JSONException e2) {
                        e = e2;
                        e.printStackTrace();
                        String str = strOptString3;
                        strOptString3 = strOptString;
                        plan_id = str;
                        reportClickNotification(context, strOptString3, plan_id, string);
                    }
                } else if ("oppo".equals(string)) {
                    String string3 = extras.getString("system_content");
                    PushLog.i(TAG, "content:" + string3);
                    new JSONObject();
                    try {
                        jSONObject2 = new JSONObject(string3);
                        strOptString = jSONObject2.optString("push_id");
                    } catch (JSONException e3) {
                        e = e3;
                        strOptString = "";
                    }
                    try {
                        strOptString3 = jSONObject2.optString("plan_id");
                    } catch (JSONException e4) {
                        e = e4;
                        e.printStackTrace();
                        String str2 = strOptString3;
                        strOptString3 = strOptString;
                        plan_id = str2;
                        reportClickNotification(context, strOptString3, plan_id, string);
                    }
                } else {
                    if ("vivo".equals(string)) {
                        String string4 = extras.getString("data");
                        PushLog.i(TAG, "content:" + string4);
                        new JSONObject();
                        try {
                            jSONObjectOptJSONObject2 = new JSONObject(string4).optJSONObject("system_content");
                            strOptString2 = jSONObjectOptJSONObject2.optString("push_id");
                        } catch (JSONException e5) {
                            e = e5;
                            strOptString2 = "";
                        }
                        try {
                            strOptString3 = jSONObjectOptJSONObject2.optString("plan_id");
                        } catch (JSONException e6) {
                            e = e6;
                            e.printStackTrace();
                            plan_id = strOptString3;
                            strOptString3 = strOptString2;
                            reportClickNotification(context, strOptString3, plan_id, string);
                        }
                    } else if ("fcm".equals(string)) {
                        String string5 = extras.getString("data");
                        PushLog.i(TAG, "content:" + string5);
                        new JSONObject();
                        try {
                            jSONObjectOptJSONObject = new JSONObject(string5).optJSONObject("system_content");
                            strOptString2 = jSONObjectOptJSONObject.optString("push_id");
                        } catch (JSONException e7) {
                            e = e7;
                            strOptString2 = "";
                        }
                        try {
                            strOptString3 = jSONObjectOptJSONObject.optString("plan_id");
                        } catch (JSONException e8) {
                            e = e8;
                            e.printStackTrace();
                            plan_id = strOptString3;
                            strOptString3 = strOptString2;
                            reportClickNotification(context, strOptString3, plan_id, string);
                        }
                    } else {
                        plan_id = "";
                    }
                    plan_id = strOptString3;
                    strOptString3 = strOptString2;
                }
                String str22 = strOptString3;
                strOptString3 = strOptString;
                plan_id = str22;
            }
            reportClickNotification(context, strOptString3, plan_id, string);
        } catch (Exception e9) {
            e9.printStackTrace();
        }
    }

    public static void reportNotificationOpened(Context context, String str, String str2) throws JSONException, PackageManager.NameNotFoundException {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                project = applicationInfo.metaData.getString("ngpush_project");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "context=" + context + ",reqId=" + str);
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("project=");
        sb.append(project);
        Log.i(str3, sb.toString());
        Log.i(TAG, "ngpushDeviceId=" + getDevId(context));
        Log.i(TAG, "packageName=" + context.getPackageName());
        Log.i(TAG, "registerId=" + getDevId(context).split(",")[0]);
        Log.i(TAG, "extendJson=" + getDevId(context).split(",")[0]);
        if ("".equals(str) || str == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (!"".equals(str2)) {
                jSONObject = new JSONObject(str2);
            }
            jSONObject.put("eventType", "clickmsg");
            jSONObject.put("project", project);
            jSONObject.put("product_id", product_id);
            jSONObject.put("reqid", str);
            jSONObject.put(NotificationCompat.GROUP_KEY_SILENT, false);
            jSONObject.put("platform", "ad");
            jSONObject.put("channeltype", "niepush");
            jSONObject.put("account", subscriber);
            jSONObject.put("token", PushSetting.getVaule(context, "token"));
            jSONObject.put("timestamp", (int) (System.currentTimeMillis() / 1000));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        UploadUtil.SendRequest(jSONObject, context);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0027 A[Catch: all -> 0x0022, IOException -> 0x0054, TRY_LEAVE, TryCatch #5 {IOException -> 0x0054, blocks: (B:7:0x001b, B:11:0x0027, B:15:0x0034, B:20:0x0040), top: B:67:0x001b, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0034 A[Catch: all -> 0x0022, IOException -> 0x0054, TRY_ENTER, TRY_LEAVE, TryCatch #5 {IOException -> 0x0054, blocks: (B:7:0x001b, B:11:0x0027, B:15:0x0034, B:20:0x0040), top: B:67:0x001b, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void readLibraryConfig() throws java.lang.IllegalAccessException, org.json.JSONException, java.io.IOException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pushclient.PushManagerImpl.readLibraryConfig():void");
    }

    public static void setEnableStartOtherService(boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.i(TAG, "PushMangerImpl setEnableStartOtherService" + z);
        if (s_initialized) {
            com.netease.inner.pushclient.PushManager.getInstance().setEnableStartOtherService(s_context, z);
        }
    }

    public static void setDefaultPermissionPromptTimes(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            if (TextUtils.isEmpty(PushSetting.getVaule(context, NOTIFICATIONS_PROMPT_TIMES))) {
                setPermissionPromptTimes(context, 1);
            }
        } catch (Exception e) {
            PushLog.e(TAG, "PushMangerImpl setDefaultPermissionPromptTimes e: " + e.getMessage());
        }
    }

    public static void setPermissionPromptTimes(Context context, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            Log.i(TAG, "PushMangerImpl setPermissionPromptTimes timesLimit=" + i);
            if (i > 0) {
                PushSetting.setKeyVaule(context, NOTIFICATIONS_PROMPT_TIMES, String.valueOf(i));
            } else {
                PushSetting.setKeyVaule(context, "notificationsPermission", "-2");
            }
        } catch (Exception e) {
            PushLog.e(TAG, "PushMangerImpl setPermissionPromptTimes e: " + e.getMessage());
        }
    }

    private static boolean isCloudDevice() {
        if (isCloudEnv != null) {
            return isCloudEnv.booleanValue();
        }
        String strCheckCloudEnvType = checkCloudEnvType();
        Log.i(TAG, "isCloudDevice -> cloudEnvType: " + strCheckCloudEnvType);
        isCloudEnv = Boolean.valueOf(strCheckCloudEnvType != null);
        return isCloudEnv.booleanValue();
    }

    private static String checkCloudEnvType() {
        try {
            Class.forName("com.netease.ntunisdk.base.SdkMgr");
            if ("1".equals(SdkMgr.getInst().getPropStr("IS_RUNNING_CLOUD", ""))) {
                return "douyin cloud";
            }
        } catch (Throwable unused) {
        }
        try {
            Class.forName("com.netease.ntunisdk.base.SdkMgr");
            if ("1".equals(SdkMgr.getInst().getPropStr("CLOUD_GAME_CLOUD", ""))) {
                return "netease cloud";
            }
        } catch (Throwable unused2) {
        }
        if (Build.MODEL.endsWith("-taptap")) {
            return "taptap cloud";
        }
        if (Build.BRAND.endsWith("-taptap")) {
            return "taptap cloud";
        }
        try {
            if ("true".equals(getSystemProperty("persist.sys.cloud_env"))) {
                return "cloud_env";
            }
        } catch (Throwable unused3) {
        }
        try {
            if ("true".equals(getSystemProperty("persist.sys.byte_cloud_env"))) {
                return "byte_cloud_env";
            }
            return null;
        } catch (Throwable unused4) {
            return null;
        }
    }

    private static String getSystemProperty(String str) throws Throwable {
        BufferedReader bufferedReader = null;
        try {
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop " + str).getInputStream()), 1024);
                try {
                    String strTrim = bufferedReader2.readLine().trim();
                    try {
                        bufferedReader2.close();
                        return strTrim;
                    } catch (Throwable unused) {
                        return strTrim;
                    }
                } catch (Exception e) {
                    e = e;
                    bufferedReader = bufferedReader2;
                    PushLog.e(TAG, "getSystemProperty -> e: " + e);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable unused2) {
                        }
                    }
                    return "";
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable unused3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }
}