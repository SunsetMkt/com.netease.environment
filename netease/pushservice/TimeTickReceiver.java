package com.netease.pushservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.base.PatchPlaceholder;
import com.netease.push.utils.PushLog;
import com.netease.push.utils.PushSetting;
import com.netease.push.utils.VersionManager;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes4.dex */
public class TimeTickReceiver extends BroadcastReceiver {
    private static final String TAG = "NGPush_" + TimeTickReceiver.class.getSimpleName();

    private void patchPlaceholder() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.i(TAG, PatchPlaceholder.class.getSimpleName());
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PushLog.d(TAG, "onReceive");
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        String packageName = context.getPackageName();
        String curPkg = PushSetting.getCurPkg(context);
        PushLog.d(TAG, "action:" + action);
        PushLog.d(TAG, "contextpkg:" + packageName);
        PushLog.d(TAG, "runningpkg:" + curPkg);
        VersionManager.VersionInfo newestInstallVersion = VersionManager.getNewestInstallVersion(context);
        if (newestInstallVersion == null || TextUtils.isEmpty(newestInstallVersion.mPackageName) || !packageName.equals(newestInstallVersion.mPackageName)) {
            return;
        }
        Intent intentCreateServiceIntent = PushServiceHelper.createServiceIntent();
        intentCreateServiceIntent.setPackage(newestInstallVersion.mPackageName);
        PushLog.d(TAG, "startService");
        PushLog.d(TAG, "intent action:" + intentCreateServiceIntent.getAction());
        PushLog.d(TAG, "intent package:" + intentCreateServiceIntent.getPackage());
        int i = 0;
        try {
            i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            PushLog.e(TAG, "PackageManager.NameNotFoundException:" + e.toString());
        }
        if (Build.VERSION.SDK_INT >= 26 && i >= 26) {
            context.startForegroundService(intentCreateServiceIntent);
        } else {
            context.startService(intentCreateServiceIntent);
        }
    }
}