package com.netease.mpay.ps.codescanner.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/* loaded from: classes6.dex */
public class ViewUtils {
    public static boolean isLandscape(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService("window");
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            return point.x > point.y;
        } catch (Throwable th) {
            Logging.logStackTrace(th);
            return true;
        }
    }

    public static boolean isFinishing(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return true;
        }
        if (17 <= Build.VERSION.SDK_INT) {
            return activity.isDestroyed();
        }
        return false;
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

    private static boolean shouldHideNavigationBar(Context context) {
        return isLandscape(context);
    }

    public static void hideNavigationBar(Window window) {
        if (window == null) {
            return;
        }
        try {
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
                decorView.setSystemUiVisibility(8);
            } else {
                decorView.setSystemUiVisibility(5894);
            }
        } catch (Throwable th) {
            Logging.logStackTrace(th);
        }
    }

    public static void refreshWindowWithNavigationBar(Window window) {
        try {
            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= 19) {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 256 | 4 | 1024);
            }
        } catch (Throwable th) {
            Logging.logStackTrace(th);
        }
    }

    public static void setNavigationBar(Context context, Window window) {
        if (shouldHideNavigationBar(context)) {
            hideNavigationBar(window);
        } else if (isLandscape(context)) {
            refreshWindowWithNavigationBar(window);
        }
    }
}