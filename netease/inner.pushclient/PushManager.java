package com.netease.inner.pushclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.netease.push.utils.AppInfo;
import com.netease.push.utils.PushConstantsImpl;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import com.netease.pushservice.PushService;
import com.netease.pushservice.PushServiceHelper;
import com.netease.pushservice.PushServiceReceiver;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public final class PushManager {
    private AppInfo mAppInfo;
    private PushServiceReceiver receiver;
    private volatile boolean registered = false;
    private static final String TAG = "NGPush_" + PushManager.class.getSimpleName() + "_inner";
    private static PushManager mServiceManager = new PushManager();
    public static boolean enableStartOtherService = false;

    private PushManager() {
    }

    public static PushManager getInstance() {
        return mServiceManager;
    }

    public void init(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, "PushManager init, ctx:" + context);
        PushSetting.setVerCode(context, PushConstantsImpl.JAR_VER_CODE);
        PushLog.d(TAG, "setVerCode, JAR_VER_CODE:342");
    }

    public String getDevId(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushService pushService = PushServiceHelper.getInstance().getPushService();
        String devId = pushService != null ? PushSetting.getDevId(pushService) : "";
        if (TextUtils.isEmpty(devId)) {
            devId = PushSetting.getDevId(context);
        }
        PushLog.d(TAG, "getDevId, pushService:" + pushService + ", devid:" + devId);
        return devId;
    }

    public String getServiceType(Context context) {
        return context != null ? PushSetting.getServiceType(context, context.getPackageName()) : "niepush";
    }

    public void setServiceType(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setServiceType, ctx:" + context + ", type:" + str);
        PushSetting.setServiceType(context, str);
    }

    public String getSenderID(Context context, String str) {
        return PushSetting.getSenderID(context, str);
    }

    public boolean setSenderID(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setSenderID, ctx:" + context + ", serviceType:" + str + ", senderID:" + str2);
        PushSetting.setSenderID(context, str, str2);
        return true;
    }

    public String getRegistrationID(Context context, String str) {
        return PushSetting.getRegistrationID(context, str);
    }

    public void setRegistrationID(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setRegistrationID, ctx:" + context + ", serviceType:" + str + ", regid:" + str2);
        PushSetting.setRegistrationID(context, str, str2);
    }

    public String getAppID(Context context, String str) {
        return PushSetting.getAppID(context, str);
    }

    public boolean setAppID(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setAppID, ctx:" + context + ", serviceType:" + str + ", appID:" + str2);
        PushSetting.setAppID(context, str, str2);
        return true;
    }

    public String getAppKey(Context context, String str) {
        return PushSetting.getAppKey(context, str);
    }

    public boolean setAppKey(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setAppKey, ctx:" + context + ", serviceType:" + str + ", appKey:" + str2);
        PushSetting.setAppKey(context, str, str2);
        return true;
    }

    public boolean enableSound(Context context, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "enableSound, ctx:" + context + ", flag:" + z);
        if (z == this.mAppInfo.mbEnableSound) {
            return true;
        }
        PushSetting.setSound(context, z);
        return true;
    }

    public boolean enableVibrate(Context context, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "enableVibrate, ctx:" + context + ", flag:" + z);
        if (z == this.mAppInfo.mbEnableVibrate) {
            return true;
        }
        PushSetting.setVibrate(context, z);
        return true;
    }

    public boolean enableLight(Context context, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "enableLight, ctx:" + context + ", flag:" + z);
        if (z == this.mAppInfo.mbEnableLight) {
            return true;
        }
        PushSetting.setLight(context, z);
        return true;
    }

    public void setEnableStartOtherService(Context context, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setEnableStartOtherService, ctx:" + context + ", enableStartOtherService:" + z);
        enableStartOtherService = z;
        PushSetting.setKeyVaule(context, "enableStartOtherService", Boolean.toString(z));
    }

    public boolean enableRepeatProtect(Context context, boolean z) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "enableRepeatProtect, ctx:" + context + ", enable:" + z);
        PushSetting.enableRepeatProtect(context, z);
        Intent intentCreateMethodIntent = PushServiceHelper.createMethodIntent();
        intentCreateMethodIntent.putExtra("method", PushConstantsImpl.SERVICE_METHOD_REPEATPROTECT);
        intentCreateMethodIntent.putExtra("package", context.getPackageName());
        intentCreateMethodIntent.putExtra(PushConstantsImpl.INTENT_FLAG_NAME, z);
        intentCreateMethodIntent.setPackage(PushSetting.getCurPkg(context));
        context.sendBroadcast(intentCreateMethodIntent, context.getPackageName() + ".permission.ngpush");
        return true;
    }

    public boolean setRepeatProtectInterval(Context context, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, "setRepeatProtectInterval, ctx:" + context + ", interval:" + i);
        PushSetting.setRepeatProtectInterval(context, i);
        Intent intentCreateMethodIntent = PushServiceHelper.createMethodIntent();
        intentCreateMethodIntent.putExtra("method", PushConstantsImpl.SERVICE_METHOD_REPEATPROTECT_INTERVAL);
        intentCreateMethodIntent.putExtra("package", context.getPackageName());
        intentCreateMethodIntent.putExtra(PushConstantsImpl.INTENT_NO_REPEAT_INTERVAL_NAME, i);
        intentCreateMethodIntent.setPackage(PushSetting.getCurPkg(context));
        context.sendBroadcast(intentCreateMethodIntent, context.getPackageName() + ".permission.ngpush");
        return true;
    }

    public void startService(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, "startService");
        String serviceType = getServiceType(context);
        PushLog.d(TAG, "serviceType:" + serviceType);
        this.mAppInfo = PushSetting.getAppInfo(context, context.getPackageName());
        PushLog.d(TAG, "mAppInfo:" + this.mAppInfo);
        Intent intent = new Intent();
        intent.putExtra(PushConstantsImpl.SERVICE_START_TYPE, PushConstantsImpl.SERVICE_START_TYPE_SELF);
        PushServiceHelper.startPushService(context, intent);
        if (PushSetting.checkUseNiepush2(context, true) && enableStartOtherService) {
            for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentServices(new Intent(PushConstantsImpl.SERVICE_ACTION3), 0)) {
                String str = resolveInfo.serviceInfo.packageName;
                if (!context.getPackageName().equalsIgnoreCase(str)) {
                    Intent intent2 = new Intent(PushConstantsImpl.SERVICE_ACTION3);
                    intent2.putExtra(PushConstantsImpl.SERVICE_START_TYPE, "other");
                    intent2.setComponent(new ComponentName(str, resolveInfo.serviceInfo.name));
                    context.startService(intent2);
                }
            }
        }
        registerServiceReceiver(context);
    }

    private void restartService(Context context, String str, String str2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.e(TAG, "restart service, ctx:" + context);
        PushLog.d(TAG, "pkgToStop:" + str);
        PushLog.d(TAG, "pkgToStart:" + str2);
        Intent intentCreateMethodIntent = PushServiceHelper.createMethodIntent();
        intentCreateMethodIntent.putExtra("method", PushConstantsImpl.SERVICE_METHOD_RESTART);
        intentCreateMethodIntent.putExtra("package", str2);
        intentCreateMethodIntent.setPackage(str);
        context.sendBroadcast(intentCreateMethodIntent, context.getPackageName() + ".permission.ngpush");
    }

    public void stopService(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Intent intent = new Intent();
        intent.setClass(context, PushService.class);
        context.stopService(intent);
        PushLog.i(TAG, "ctx.stopService!!!!");
        if (this.receiver != null) {
            context.getApplicationContext().unregisterReceiver(this.receiver);
        }
        this.registered = false;
    }

    private void registerServiceReceiver(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (this.registered) {
            return;
        }
        PushLog.i(TAG, "registerServiceReceiver, ctx:" + context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addCategory("android.intent.category.LAUNCHER");
        this.receiver = new PushServiceReceiver();
        context.getApplicationContext().registerReceiver(this.receiver, intentFilter);
        this.registered = true;
    }
}