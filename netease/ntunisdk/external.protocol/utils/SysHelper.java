package com.netease.ntunisdk.external.protocol.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.browser.customtabs.CustomTabsService;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.view.CustomTab;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SysHelper {
    private static final String[] BROWSER_PACKAGES = {"com.android.chrome", "com.chrome.beta", "com.chrome.dev", "org.mozilla.firefox", "com.microsoft.emmx"};
    private static final String TAG = "H";

    public static void exitProcessInLaunch(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        }
        try {
            System.exit(0);
        } catch (Throwable unused) {
        }
        try {
            Process.killProcess(Process.myPid());
        } catch (Throwable unused2) {
        }
    }

    public static void openBrowser(Activity activity, String str) {
        L.d("H", "openBrowser:" + str);
        try {
            try {
                if (!new CustomTab(str).openCustomTab(activity)) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str.trim()));
                    intent.putExtra("com.android.browser.application_id", activity.getPackageName());
                    HookManager.startActivity(activity, intent);
                }
            } catch (Throwable unused) {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str.trim()));
                intent2.putExtra("com.android.browser.application_id", activity.getPackageName());
                HookManager.startActivity(activity, intent2);
            }
        } catch (Exception unused2) {
        }
    }

    public static String getChromePackage(Context context) {
        List<ResolveInfo> listQueryIntentServices = context.getPackageManager().queryIntentServices(new Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION), 0);
        if (listQueryIntentServices == null) {
            return null;
        }
        HashSet hashSet = new HashSet(Arrays.asList(BROWSER_PACKAGES));
        Iterator<ResolveInfo> it = listQueryIntentServices.iterator();
        while (it.hasNext()) {
            ServiceInfo serviceInfo = it.next().serviceInfo;
            if (serviceInfo != null && hashSet.contains(serviceInfo.packageName)) {
                return serviceInfo.packageName;
            }
        }
        return null;
    }

    public static void killProcess(Context context) {
        if (SDKRuntime.getInstance().isNotExitProcess() || context == null) {
            return;
        }
        if ((context instanceof Activity) && Build.VERSION.SDK_INT >= 16) {
            ((Activity) context).finishAffinity();
        }
        try {
            int iMyPid = Process.myPid();
            String str = "";
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
            Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ActivityManager.RunningAppProcessInfo next = it.next();
                if (iMyPid == next.pid) {
                    str = next.processName;
                    break;
                }
            }
            L.d("H", "mainProcessName = " + str);
            if (TextUtils.isEmpty(str)) {
                Process.killProcess(iMyPid);
                return;
            }
            ArrayList<Integer> arrayList = new ArrayList();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                L.d("H", runningAppProcessInfo.processName);
                if (runningAppProcessInfo.processName.contains(str) && !runningAppProcessInfo.processName.equals(str)) {
                    L.d("H", "add " + runningAppProcessInfo.pid);
                    arrayList.add(Integer.valueOf(runningAppProcessInfo.pid));
                }
            }
            for (Integer num : arrayList) {
                L.d("H", "id = " + num);
                Process.killProcess(num.intValue());
            }
            Process.killProcess(iMyPid);
        } catch (Throwable unused) {
            try {
                System.exit(0);
            } catch (Throwable unused2) {
            }
        }
    }

    public static boolean isAppAlive(Context context) {
        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        for (int i = 0; i < runningAppProcesses.size(); i++) {
            if (runningAppProcesses.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void hideSystemUI(Window window) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            window.setAttributes(attributes);
        }
        try {
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
                decorView.setSystemUiVisibility(8);
            } else if (Build.VERSION.SDK_INT >= 19) {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 512 | 256 | 1024 | 2 | 4096 | 4);
            }
        } catch (Throwable unused) {
        }
    }

    public static void clearDialogFocusable(Window window) {
        if (window == null) {
            return;
        }
        window.addFlags(8);
    }

    public static void resetDialogFocusable(Window window) {
        if (window == null) {
            return;
        }
        window.clearFlags(8);
    }

    public static boolean isSupportHttpDNS() throws ClassNotFoundException {
        try {
            Class.forName("com.netease.mpay.httpdns.HttpDns");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}