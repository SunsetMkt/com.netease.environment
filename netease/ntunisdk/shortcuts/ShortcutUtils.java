package com.netease.ntunisdk.shortcuts;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.netease.mc.mi.R;
import com.netease.ntunisdk.SdkQRCode;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ShortcutUtils {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private static final List<CheckItem> CHECK_UNI_SDK_PARAMS_LIST = new ArrayList<CheckItem>() { // from class: com.netease.ntunisdk.shortcuts.ShortcutUtils.1
        AnonymousClass1() {
            add(new CheckItem(ConstProp.UNISDK_JF_GAS3_URL, 1001));
            add(new CheckItem(ConstProp.JF_LOG_KEY, 1002));
        }
    };
    public static final String PROTOCOL_LAUNCHER = "com.netease.ntunisdk.external.protocol.ProtocolLauncher";
    private static final String TAG = "QR shortcut";

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

    public static boolean isProtocolLauncher(Context context) {
        String launcherName = getLauncherName(context);
        UniSdkUtils.d(TAG, "launcherName = " + launcherName);
        return "com.netease.ntunisdk.external.protocol.ProtocolLauncher".equals(launcherName);
    }

    public static String getLauncherName(Context context) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        return (launchIntentForPackage == null || launchIntentForPackage.getComponent() == null) ? "" : launchIntentForPackage.getComponent().getClassName();
    }

    public static void addShortcut(Context context, String str) {
        UniSdkUtils.d(TAG, "addShortcut");
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(context.getResources(), R.drawable.ntunisdk_open_album_selected);
        if (Build.VERSION.SDK_INT >= 26) {
            addPinShortcut(context, getShortCutIntent(context), str, bitmapDecodeResource);
        } else {
            addShortcutInner(context, getShortCutIntent(context), str, false, bitmapDecodeResource);
        }
        bitmapDecodeResource.recycle();
    }

    private static Intent getShortCutIntent(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setClass(context, ShortcutMainActivity.class);
        return intent;
    }

    private ShortcutUtils() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    private static void addPinShortcut(Context context, Intent intent, String str, Bitmap bitmap) {
        UniSdkUtils.d(TAG, "addPinShortcut");
        ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(ShortcutManager.class);
        if (shortcutManager.isRequestPinShortcutSupported()) {
            ShortcutInfo shortcutInfoBuild = new ShortcutInfo.Builder(context, str).setIntent(intent).setIcon(Icon.createWithBitmap(bitmap)).setShortLabel("short_label").setLongLabel("long_label").build();
            shortcutManager.requestPinShortcut(shortcutInfoBuild, PendingIntent.getBroadcast(context, 0, shortcutManager.createShortcutResultIntent(shortcutInfoBuild), 67108864).getIntentSender());
        }
    }

    private static void addShortcutInner(Context context, Intent intent, String str, boolean z, Bitmap bitmap) {
        UniSdkUtils.d(TAG, "addShortcutInner");
        Intent intent2 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent2.putExtra("duplicate", z);
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("android.intent.extra.shortcut.ICON", bitmap);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        context.sendBroadcast(intent2);
    }

    private static void removeShortcutInner(Context context, Intent intent, String str) {
        UniSdkUtils.d(TAG, "removeShortcutInner");
        Intent intent2 = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        intent2.putExtra("android.intent.extra.shortcut.NAME", str);
        intent2.putExtra("duplicate", false);
        intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
        context.sendBroadcast(intent2);
    }

    public static void openGame(Activity activity) {
        HookManager.startActivity(activity, activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
        activity.overridePendingTransition(0, 0);
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
            UniSdkUtils.d("QR quickqr_main", e.getMessage());
            return false;
        } catch (Throwable th) {
            UniSdkUtils.d("QR quickqr_main", th.getMessage());
            return false;
        }
    }

    /* renamed from: com.netease.ntunisdk.shortcuts.ShortcutUtils$1 */
    class AnonymousClass1 extends ArrayList<CheckItem> {
        AnonymousClass1() {
            add(new CheckItem(ConstProp.UNISDK_JF_GAS3_URL, 1001));
            add(new CheckItem(ConstProp.JF_LOG_KEY, 1002));
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
}