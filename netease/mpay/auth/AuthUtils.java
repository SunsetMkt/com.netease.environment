package com.netease.mpay.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.netease.ntunisdk.SdkQRCode;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AuthUtils {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private static final List<CheckItem> CHECK_UNI_SDK_PARAMS_LIST = new ArrayList<CheckItem>() { // from class: com.netease.mpay.auth.AuthUtils.1
        {
            add(new CheckItem(ConstProp.UNISDK_JF_GAS3_URL, 1001));
            add(new CheckItem(ConstProp.JF_LOG_KEY, 1002));
        }
    };
    private static final String TAG = "OAuth Credential";

    public static void relaunchApp(Activity activity, boolean z) {
        Intent launchIntentForPackage = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        if (launchIntentForPackage == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.");
            return;
        }
        launchIntentForPackage.addFlags(335577088);
        activity.getApplication().startActivity(launchIntentForPackage);
        if (z) {
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    public static boolean disableQuickQrPay() {
        return 1 == SdkMgr.getInst().getPropInt(SdkQRCode.DISABLE_QUICK_QR_PAY, 0);
    }

    public static String getLauncherName(Context context) {
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
    }

    private static Intent getShortCutIntent(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setClass(context, AuthActivity.class);
        return intent;
    }

    private AuthUtils() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static boolean isSupportProtocol() {
        return isClassInstalled("com.netease.ntunisdk.base.utils.ResUtils") && isClassInstalled("com.netease.ntunisdk.external.protocol.ProtocolManager");
    }

    public static boolean isUniSDKDeepLinkSupport(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(new Uri.Builder().scheme("ntes").authority("game.mobile").path(str).build());
        return context.getPackageManager().resolveActivity(intent, 65536) != null;
    }

    public static boolean isClassInstalled(String str) {
        try {
            return Class.forName(str) != null;
        } catch (Exception e) {
            UniSdkUtils.d(TAG, e.getMessage());
            return false;
        } catch (Throwable th) {
            UniSdkUtils.d(TAG, th.getMessage());
            return false;
        }
    }

    public static CheckResult checkUniSDKParams() {
        if (disableQuickQrPay()) {
            return CheckResult.CHECK_SUCCESS;
        }
        for (CheckItem checkItem : CHECK_UNI_SDK_PARAMS_LIST) {
            if (TextUtils.isEmpty(SdkMgr.getInst().getPropStr(checkItem.checkKey))) {
                return CheckResult.newCheckFailure(checkItem.checkCode);
            }
        }
        return CheckResult.CHECK_SUCCESS;
    }

    private static class CheckItem {
        final int checkCode;
        final String checkKey;

        public CheckItem(String str, int i) {
            this.checkKey = str;
            this.checkCode = i;
        }
    }

    static class CheckResult {
        public static CheckResult CHECK_SUCCESS = new CheckResult(true, 0);
        final int code;
        final boolean success;

        public static CheckResult newCheckFailure(int i) {
            return new CheckResult(false, i);
        }

        public CheckResult(boolean z, int i) {
            this.success = z;
            this.code = i;
        }
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
}