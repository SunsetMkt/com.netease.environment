package com.netease.androidcrashhandler.thirdparty.deviceInfoModule;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DeviceInfoProxy {
    public static void initDeviceInfoModule(Context context) {
        ModulesManager.getInst().init(context);
    }

    public static void getUniqueData(Context context) throws JSONException {
        Log.i(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] start");
        if (context == null) {
            Log.w(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] context error");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getImei");
            String strExtendFunc = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            LogUtils.i(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] imei=" + strExtendFunc);
            InitProxy.getInstance().setmImei(strExtendFunc);
            jSONObject.put("methodId", "getTransId");
            String strExtendFunc2 = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            Log.i(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] transId=" + strExtendFunc2);
            InitProxy.getInstance().setmTransid(strExtendFunc2);
            jSONObject.put("methodId", "getUnisdkDeviceId");
            String strExtendFunc3 = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            Log.i(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] unisdkDeviceId=" + strExtendFunc3);
            InitProxy.getInstance().setmUnisdkDeviceId(strExtendFunc3);
            jSONObject.put("methodId", "getLocalIpAddress");
            String strExtendFunc4 = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            Log.i(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] localIpAddress=" + strExtendFunc4);
            InitProxy.getInstance().setmLocalIp(strExtendFunc4);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "DeviceInfoProxy [getUniqueData] Exception=" + e.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0084 A[Catch: Exception -> 0x00b6, TRY_LEAVE, TryCatch #0 {Exception -> 0x00b6, blocks: (B:7:0x001f, B:10:0x002d, B:28:0x0084, B:12:0x0037, B:14:0x003d, B:15:0x0046, B:17:0x004c, B:18:0x0055, B:20:0x005b, B:21:0x0064, B:23:0x006a, B:24:0x0073, B:26:0x0079), top: B:35:0x001f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean setUniqueData(java.lang.String r13, java.lang.String r14) throws org.json.JSONException {
        /*
            java.lang.String r0 = "timeArea"
            java.lang.String r1 = "timeZone"
            java.lang.String r2 = "macAddress"
            java.lang.String r3 = "androidId"
            java.lang.String r4 = "imsi"
            java.lang.String r5 = "imei"
            java.lang.String r6 = "DeviceInfoProxy [setUniqueData] paramJson="
            boolean r7 = android.text.TextUtils.isEmpty(r13)
            java.lang.String r8 = "trace"
            r9 = 0
            if (r7 != 0) goto Ld0
            boolean r7 = android.text.TextUtils.isEmpty(r14)
            if (r7 == 0) goto L1f
            goto Ld0
        L1f:
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lb6
            r7.<init>()     // Catch: java.lang.Exception -> Lb6
            boolean r10 = r5.equals(r13)     // Catch: java.lang.Exception -> Lb6
            r11 = 1
            java.lang.String r12 = "methodId"
            if (r10 == 0) goto L37
            java.lang.String r13 = "setImei"
            r7.put(r12, r13)     // Catch: java.lang.Exception -> Lb6
            r7.put(r5, r14)     // Catch: java.lang.Exception -> Lb6
        L35:
            r9 = r11
            goto L82
        L37:
            boolean r5 = r4.equals(r13)     // Catch: java.lang.Exception -> Lb6
            if (r5 == 0) goto L46
            java.lang.String r13 = "setImsi"
            r7.put(r12, r13)     // Catch: java.lang.Exception -> Lb6
            r7.put(r4, r14)     // Catch: java.lang.Exception -> Lb6
            goto L35
        L46:
            boolean r4 = r3.equals(r13)     // Catch: java.lang.Exception -> Lb6
            if (r4 == 0) goto L55
            java.lang.String r13 = "setAndroidId"
            r7.put(r12, r13)     // Catch: java.lang.Exception -> Lb6
            r7.put(r3, r14)     // Catch: java.lang.Exception -> Lb6
            goto L35
        L55:
            boolean r3 = r2.equals(r13)     // Catch: java.lang.Exception -> Lb6
            if (r3 == 0) goto L64
            java.lang.String r13 = "setMacAddress"
            r7.put(r12, r13)     // Catch: java.lang.Exception -> Lb6
            r7.put(r2, r14)     // Catch: java.lang.Exception -> Lb6
            goto L35
        L64:
            boolean r2 = r1.equals(r13)     // Catch: java.lang.Exception -> Lb6
            if (r2 == 0) goto L73
            java.lang.String r13 = "setTimeZone"
            r7.put(r12, r13)     // Catch: java.lang.Exception -> Lb6
            r7.put(r1, r14)     // Catch: java.lang.Exception -> Lb6
            goto L35
        L73:
            boolean r13 = r0.equals(r13)     // Catch: java.lang.Exception -> Lb6
            if (r13 == 0) goto L82
            java.lang.String r13 = "setTimeArea"
            r7.put(r12, r13)     // Catch: java.lang.Exception -> Lb6
            r7.put(r0, r14)     // Catch: java.lang.Exception -> Lb6
            goto L35
        L82:
            if (r9 == 0) goto Lcf
            com.netease.ntunisdk.modules.api.ModulesManager r13 = com.netease.ntunisdk.modules.api.ModulesManager.getInst()     // Catch: java.lang.Exception -> Lb6
            com.netease.androidcrashhandler.NTCrashHunterKit r14 = com.netease.androidcrashhandler.NTCrashHunterKit.sharedKit()     // Catch: java.lang.Exception -> Lb6
            android.content.Context r14 = r14.getContext()     // Catch: java.lang.Exception -> Lb6
            r13.init(r14)     // Catch: java.lang.Exception -> Lb6
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb6
            r13.<init>(r6)     // Catch: java.lang.Exception -> Lb6
            java.lang.String r14 = r7.toString()     // Catch: java.lang.Exception -> Lb6
            r13.append(r14)     // Catch: java.lang.Exception -> Lb6
            java.lang.String r13 = r13.toString()     // Catch: java.lang.Exception -> Lb6
            android.util.Log.i(r8, r13)     // Catch: java.lang.Exception -> Lb6
            com.netease.ntunisdk.modules.api.ModulesManager r13 = com.netease.ntunisdk.modules.api.ModulesManager.getInst()     // Catch: java.lang.Exception -> Lb6
            java.lang.String r14 = "crashhunter"
            java.lang.String r0 = "deviceInfo"
            java.lang.String r1 = r7.toString()     // Catch: java.lang.Exception -> Lb6
            r13.extendFunc(r14, r0, r1)     // Catch: java.lang.Exception -> Lb6
            goto Lcf
        Lb6:
            r13 = move-exception
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            java.lang.String r0 = "DeviceInfoProxy [setUniqueData] Exception="
            r14.<init>(r0)
            java.lang.String r0 = r13.toString()
            r14.append(r0)
            java.lang.String r14 = r14.toString()
            android.util.Log.i(r8, r14)
            r13.printStackTrace()
        Lcf:
            return r9
        Ld0:
            java.lang.String r13 = "DeviceInfoProxy [setUniqueData] params error"
            android.util.Log.w(r8, r13)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.thirdparty.deviceInfoModule.DeviceInfoProxy.setUniqueData(java.lang.String, java.lang.String):boolean");
    }

    public static JSONObject getNetworkInfo() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getNetworkInfoJson");
            jSONObject.put("from", "NetConnectivity");
            String strExtendFunc = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            LogUtils.i(LogUtils.TAG, "DeviceInfoProxy [isConnectedWifi] networkInfoJson=" + strExtendFunc);
            return new JSONObject(strExtendFunc);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "DeviceInfoProxy [isConnectedWifi] Exception=" + e.toString());
            return null;
        }
    }

    public static void createTransid() throws JSONException, NumberFormatException {
        LogUtils.i(LogUtils.TAG, "DeviceInfoProxy [createTransid] start");
        String str = InitProxy.getInstance().getmTransid();
        LogUtils.i(LogUtils.TAG, "DeviceInfoProxy [createTransid] transId=" + str);
        if (checkTransidValid(str)) {
            return;
        }
        createTransidUseDeviceInfoModule();
        if (checkTransidValid()) {
            return;
        }
        createTransidBackup();
    }

    private static void createTransidUseDeviceInfoModule() throws JSONException {
        LogUtils.i(LogUtils.TAG, "DeviceInfoProxy [createTransidUseDeviceInfoModule] start");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getTransId");
            updateTransidToRelatedBusiness(ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LogUtils.TAG, "DeviceInfoProxy [createTransidUseDeviceInfoModule] Exception=" + e.toString());
        }
    }

    private static void createTransidBackup() throws NumberFormatException {
        LogUtils.e(LogUtils.TAG, "UploadZipRequest net [createTransidBackup] transid backup");
        updateTransidToRelatedBusiness(CUtil.transIdBackup());
    }

    private static void updateTransidToRelatedBusiness(String str) throws NumberFormatException {
        LogUtils.i(LogUtils.TAG, "DeviceInfoProxy [updateTransidToRelatedBusiness] transId=" + str);
        InitProxy.getInstance().setmTransid(str);
        NTCrashHunterKit.sharedKit().setParam(ClientLogConstant.TRANSID, InitProxy.getInstance().getmTransid());
    }

    public static boolean checkTransidValid() {
        String str = InitProxy.getInstance().getmTransid();
        return (TextUtils.isEmpty(str) || str.startsWith("{")) ? false : true;
    }

    public static boolean checkTransidValid(String str) {
        return (TextUtils.isEmpty(str) || str.startsWith("{")) ? false : true;
    }
}