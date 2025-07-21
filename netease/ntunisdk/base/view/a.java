package com.netease.ntunisdk.base.view;

import android.content.Context;
import android.view.Window;
import com.netease.ntunisdk.base.utils.ResUtils;

/* compiled from: ConfigUtils.java */
/* loaded from: classes5.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f1852a;

    public static void updateNavigationBarState(boolean z) {
        f1852a = z;
    }

    private static boolean a(Context context) {
        try {
            return context.getResources().getBoolean(ResUtils.getResId(context, "unisdk__config_landscape", "bool"));
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void a(Context context, Window window) {
        if (a(context) && !f1852a) {
            ViewUtils.hideNavigationBar(window);
        } else if (a(context)) {
            ViewUtils.refreshWindowWithNavigationBar(window);
        }
    }
}