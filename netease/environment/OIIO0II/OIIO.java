package com.netease.environment.OIIO0II;

import android.util.Log;

/* compiled from: LogUtils.java */
/* loaded from: classes5.dex */
public class OIIO {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static boolean f1558OIIO00I;

    public static void OIIO00I(String str, String str2) {
        if (!f1558OIIO00I || str == null || str2 == null) {
            return;
        }
        Log.e("EnvSDK_" + str, str2);
    }

    public static void OIIO0O0(String str, String str2) {
        if (!f1558OIIO00I || str == null || str2 == null) {
            return;
        }
        Log.i("EnvSDK_" + str, str2);
    }

    public static void OIIO00I(boolean z) {
        f1558OIIO00I = z;
    }

    public static boolean OIIO00I() {
        return f1558OIIO00I;
    }
}