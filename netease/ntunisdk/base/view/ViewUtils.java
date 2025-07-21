package com.netease.ntunisdk.base.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;

/* loaded from: classes5.dex */
public class ViewUtils {
    public static boolean isFinishing(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return true;
        }
        return activity.isDestroyed();
    }

    public static Drawable getDrawable(Resources resources, int i) {
        return resources.getDrawable(i, null);
    }

    public static void setDialogUnFocusable(Window window) {
        if (window == null) {
            return;
        }
        window.addFlags(8);
    }

    public static void clearDialogUnFocusable(Window window) {
        if (window == null) {
            return;
        }
        window.clearFlags(8);
    }

    public static void hideNavigationBar(Window window) {
        if (window == null) {
            return;
        }
        try {
            window.getDecorView().setSystemUiVisibility(5894);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void refreshWindowWithNavigationBar(Window window) {
        try {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 256 | 4 | 1024);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static boolean openBrowser(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        try {
            intent.setData(Uri.parse(str));
            return startActivity(context, intent, false);
        } catch (NullPointerException e) {
            UniSdkUtils.d("ViewUtils", e.getMessage());
            return false;
        } catch (Exception e2) {
            UniSdkUtils.d("ViewUtils", e2.getMessage());
            return false;
        }
    }

    public static boolean startActivity(Context context, Intent intent, boolean z) {
        if (context != null && intent != null) {
            if (z && context.getPackageManager().resolveActivity(intent, 65536) == null) {
                return false;
            }
            try {
                HookManager.startActivity(context, intent);
                return true;
            } catch (ActivityNotFoundException e) {
                UniSdkUtils.d("ViewUtils", e.getMessage());
            } catch (Exception e2) {
                UniSdkUtils.d("ViewUtils", e2.getMessage());
                return false;
            }
        }
        return false;
    }
}