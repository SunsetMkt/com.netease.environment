package com.netease.push.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.pushservice.PushService;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class VersionManager {
    private static final String TAG = "NGPush_" + VersionManager.class.getSimpleName();

    public static class VersionInfo {
        public Boolean mNeedNiepush;
        public String mPackageName;
        public int mVersionCode;

        private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            PushLog.i(VersionManager.TAG, PatchPlaceholder.class.getSimpleName());
        }

        public VersionInfo(int i, String str, Boolean bool) {
            this.mVersionCode = 0;
            this.mPackageName = "";
            this.mNeedNiepush = false;
            this.mVersionCode = i;
            this.mPackageName = str;
            this.mNeedNiepush = bool;
        }
    }

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    public static VersionInfo getNewestInstallVersion(Context context) {
        return getNewestInstallVersionBySelf(context);
    }

    public static VersionInfo getNewestInstallVersionBySelf(Context context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int i = 0;
        Iterator<ResolveInfo> it = context.getPackageManager().queryIntentServices(new Intent(PushConstantsImpl.SERVICE_ACTION2), 0).iterator();
        String str = "";
        Boolean bool = false;
        while (it.hasNext()) {
            String str2 = it.next().serviceInfo.packageName;
            int verCode = PushSetting.getVerCode(context, str2);
            String serviceType = PushSetting.getServiceType(context, str2);
            PushLog.d(TAG, "packageName=" + str2);
            PushLog.d(TAG, "verCode=" + verCode);
            PushLog.d(TAG, "serviceType=" + serviceType);
            if (verCode > i) {
                if (!"niepush".equals(serviceType)) {
                    bool = false;
                } else {
                    bool = true;
                }
                str = str2;
                i = verCode;
            }
            if (verCode == i && context.getPackageName().equalsIgnoreCase(str2)) {
                if (!"niepush".equals(serviceType)) {
                    bool = false;
                } else {
                    bool = true;
                }
                str = str2;
                i = verCode;
            }
        }
        if (i == 0) {
            return null;
        }
        PushLog.d(TAG, "newPackage\uff1a" + str + ", newVerCode:" + i + ", needNiepush:" + bool);
        return new VersionInfo(i, str, bool);
    }

    public static boolean isPackageInstalled(String str, Context context) throws PackageManager.NameNotFoundException {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public static boolean isServiceRunning(Context context, String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "check isServiceRunning:" + str);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            String className = runningServiceInfo.service.getClassName();
            String packageName = runningServiceInfo.service.getPackageName();
            if (PushService.class.getName().equalsIgnoreCase(className) && str.equalsIgnoreCase(packageName)) {
                PushLog.d(TAG, "pkgToStart:" + str + "is running");
                return true;
            }
        }
        return false;
    }
}