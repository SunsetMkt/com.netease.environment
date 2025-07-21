package com.netease.cc.ccplayerwrapper.utils;

import android.util.Log;
import tv.danmaku.ijk.media.player.tools.PlayerHelper;

/* loaded from: classes5.dex */
public class LogUtil {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f1543a;

    public static void LOGD(String str) {
        if (f1543a) {
            Log.d("CCPlayer", str);
        }
    }

    public static void LOGE(String str) {
        if (f1543a) {
            Log.e("CCPlayer", str);
        }
        LOGF(str);
    }

    public static void LOGF(String str) {
        LOGF("CCPlayer", str);
    }

    public static void LOGI(String str) {
        if (f1543a) {
            Log.i("CCPlayer", str);
        }
    }

    public static void LOGV(String str) {
        if (f1543a) {
            Log.v("CCPlayer", str);
        }
    }

    public static void LOGW(String str) {
        if (f1543a) {
            Log.w("CCPlayer", str);
        }
    }

    public static void setLogEnable(boolean z) {
        f1543a = z;
    }

    public static void LOGF(String str, String str2) {
        PlayerHelper.log2File(str, str2);
    }

    public static void LOGD(String str, String str2) {
        if (f1543a) {
            Log.d(str, str2);
        }
    }

    public static void LOGI(String str, String str2) {
        if (f1543a) {
            Log.i(str, str2);
        }
    }

    public static void LOGV(String str, String str2) {
        if (f1543a) {
            Log.v(str, str2);
        }
    }

    public static void LOGW(String str, String str2) {
        if (f1543a) {
            Log.w(str, str2);
        }
    }

    public static void LOGE(String str, String str2) {
        if (f1543a) {
            Log.e(str, str2);
        }
        PlayerHelper.log2File(str, str2);
    }
}